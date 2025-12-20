package osrm;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OsrmClient {

    private static final Logger LOGGER = Logger.getLogger(OsrmClient.class.getName());
    
    // PART D TIP: Change this to "http://localhost:5000/table/v1/driving/" when using Docker
    private static final String BASE_URL = "https://router.project-osrm.org/table/v1/driving/";
    
    // QoS: Reduced timeouts to 2 seconds to keep the Orchestrator responsive
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2)) 
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    public DistanceResult calculateDistance(double lat1, double lon1, double lat2, double lon2) 
            throws OsrmException, OsrmServiceException {
        
        if (!isValidCoordinate(lat1, lon1) || !isValidCoordinate(lat2, lon2)) {
            throw new OsrmException("Invalid coordinates provided.", OsrmException.ErrorType.INVALID_COORDINATES);
        }

        // Use %s to allow easy switching between Web and Docker URLs
        String apiUrl = String.format("%s%f,%f;%f,%f?annotations=distance,duration", 
                                      BASE_URL, lon1, lat1, lon2, lat2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(3)) // Give it 3 seconds total to finish
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            LOGGER.info("Requesting distance from OSRM: " + apiUrl);
            
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            
            // Handle Rate Limiting (Common with the public OSRM site)
            // Handle Rate Limiting (Common with the public OSRM site)
            if (statusCode == 429) {
                LOGGER.warning("OSRM Rate Limit Hit (429). Switch to Docker for Part D!");
                // CHANGED: Using your specific Service Exception
                throw new OsrmServiceException("Rate limit exceeded on public API.");
            }

            if (statusCode == 200) {
                OsrmResponse osrm = mapper.readValue(response.body(), OsrmResponse.class);
                if (osrm.distances == null || osrm.distances[0][1] == 0) {
                    return new DistanceResult(0.0, 0.0);
                }
                return new DistanceResult(osrm.distances[0][1], osrm.durations[0][1]);
            } else {
                throw new OsrmServiceException("OSRM error: " + statusCode);
            }

        } catch (HttpConnectTimeoutException e) {
            LOGGER.log(Level.WARNING, "OSRM Timeout! Orchestrator continuing with fallback data.");
            // CHANGED: Using your specific Service Exception for consistency
            throw new OsrmServiceException("OSRM timed out - check internet or switch to Docker.", e);
        } catch (OsrmException e) {
            // This catches both OsrmException AND OsrmServiceException
            throw e; 
        } catch (Exception e) {
            // This catches everything else (like Jackson parsing errors)
            LOGGER.log(Level.SEVERE, "Unexpected error during OSRM call", e);
            throw new OsrmException("Internal system error in distance calculation", 
                                    OsrmException.ErrorType.UNEXPECTED_ERROR, e);
        }
    }

    private boolean isValidCoordinate(double lat, double lon) {
        return lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180;
    }
}