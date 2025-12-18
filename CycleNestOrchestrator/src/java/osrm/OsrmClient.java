package osrm;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OsrmClient {

    private static final Logger LOGGER = Logger.getLogger(OsrmClient.class.getName());
    
    // Move URL to a constant or config file for better maintainability
    private static final String BASE_URL = "https://router.project-osrm.org/table/v1/driving/";
    
    // Reuse HttpClient for connection pooling (QoS Improvement)
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    public DistanceResult calculateDistance(double lat1, double lon1, double lat2, double lon2) 
            throws OsrmException {
        
        // 1. Validation: Prevent unnecessary network calls
        if (!isValidCoordinate(lat1, lon1) || !isValidCoordinate(lat2, lon2)) {
            // Throw with INVALID_COORDINATES type
            throw new OsrmException("Invalid coordinates provided.", OsrmException.ErrorType.INVALID_COORDINATES);
        }

        String apiUrl = String.format("%s%f,%f;%f,%f?annotations=distance,duration", 
                                      BASE_URL, lon1, lat1, lon2, lat2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(5))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            LOGGER.info("Requesting distance from OSRM: " + apiUrl);
            
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode == 200) {
                OsrmResponse osrm = mapper.readValue(response.body(), OsrmResponse.class);
                return new DistanceResult(osrm.distances[0][1], osrm.durations[0][1]);
            } else if (statusCode >= 500) {
                LOGGER.severe("OSRM Server Error: " + statusCode);
                // Throw with SERVICE_UNAVAILABLE type
                throw new OsrmException("OSRM service is currently unavailable.", OsrmException.ErrorType.SERVICE_UNAVAILABLE);
            } else {
                LOGGER.warning("OSRM Client Error: " + statusCode + " Body: " + response.body());
                // Throw with UNEXPECTED_ERROR type for other status codes
                throw new OsrmException("Failed to calculate distance: " + statusCode, OsrmException.ErrorType.UNEXPECTED_ERROR);
            }

        } catch (OsrmException e) {
            // Re-throw OsrmExceptions so they don't get wrapped in the general catch
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during OSRM call", e);
            // Throw with UNEXPECTED_ERROR type and include the cause
            throw new OsrmException("Internal system error in distance calculation", OsrmException.ErrorType.UNEXPECTED_ERROR, e);
        }       }
    private boolean isValidCoordinate(double lat, double lon) {
        return lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180;
    }
}