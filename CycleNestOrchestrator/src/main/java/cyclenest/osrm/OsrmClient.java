package cyclenest.osrm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OsrmClient {

    private static final Logger LOGGER = Logger.getLogger(OsrmClient.class.getName());
    
    // host.docker.internal allows the container to talk to the OSRM instance on the host machine
    private static final String BASE_URL = "http://host.docker.internal:5000/route/v1/driving/";
    
    // Using a static client to reuse connections and improve performance
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2)) // Fail fast if the service is down
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    public DistanceResult calculateDistance(double lat1, double lon1, double lat2, double lon2) 
            throws OsrmException, OsrmServiceException {
        
        // Basic validation to prevent unnecessary API calls
        if (!isValidCoordinate(lat1, lon1) || !isValidCoordinate(lat2, lon2)) {
            throw new OsrmException("Invalid coordinates.", OsrmException.ErrorType.INVALID_COORDINATES);
        }

        // OSRM requires Lon,Lat order. We format the URL for a standard route request.
        String apiUrl = String.format("%s%f,%f;%f,%f?overview=false", 
                                     BASE_URL, lon1, lat1, lon2, lat2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(3)) // Global timeout for the request
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            LOGGER.info("Sending request to OSRM: " + apiUrl);
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            // Handle rate limiting specifically for API reliability
            if (statusCode == 429) {
                throw new OsrmServiceException("OSRM rate limit reached.");
            }

            if (statusCode == 200) {
                // Parse the response tree to extract distance and duration from the first route
                JsonNode root = mapper.readTree(response.body());
                JsonNode routes = root.path("routes");

                if (routes.isArray() && !routes.isEmpty()) {
                    JsonNode route = routes.get(0);
                    double dist = route.path("distance").asDouble();
                    double dur = route.path("duration").asDouble();
                    return new DistanceResult(dist, dur);
                }
                
                // Return 0 if no valid route could be calculated by the engine
                return new DistanceResult(0.0, 0.0);
            } else {
                throw new OsrmServiceException("OSRM service returned error code: " + statusCode);
            }

        } catch (OsrmException e) {
            throw e; // Pass specific exceptions up
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Internal error during OSRM communication", e);
            throw new OsrmException("Orchestrator failed to process distance", 
                                    OsrmException.ErrorType.UNEXPECTED_ERROR, e);
        }
    }

    private boolean isValidCoordinate(double lat, double lon) {
        return lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180;
    }
}