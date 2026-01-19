package cyclenest.osrm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * OsrmClient - Handles the HTTP communication with our OSRM Docker container.
 * This satisfies the Part B requirement: Consuming a Remote Web Service.
 */
public class OsrmClient {

    // Internal Docker network URL (service name 'osrm' defined in docker-compose)
    private static final String OSRM_URL = "http://osrm:5000/route/v1/driving/";

    /**
     * getRoute - Fetches distance and duration data from the routing engine.
     */
    public DistanceResult getRoute(double userLat, double userLon, double itemLat, double itemLon) 
            throws OsrmException, OsrmServiceException { 
        
        try {
            // OSRM expects coordinates in {longitude},{latitude} format.
            // Using Locale.US to ensure decimals use dots (.), not commas (,), regardless of system language.
            String coords = String.format(Locale.US, "%f,%f;%f,%f", 
                                          userLon, userLat, itemLon, itemLat);
            
            String targetUrl = OSRM_URL + coords + "?overview=false";
            
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            
            // Short timeouts so the app doesn't hang if the Docker service is down
            connection.setConnectTimeout(2000); 
            connection.setReadTimeout(2000);    
            
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new OsrmServiceException("OSRM server returned an error code: " + responseCode);
            }

            // Read the JSON response from the stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawJson = new StringBuilder();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                rawJson.append(inputLine);
            }
            reader.close();

            // Use Jackson to parse the specific bits we need from the OSRM response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawJson.toString());
            
            JsonNode routes = root.path("routes");
            if (routes.isArray() && routes.size() > 0) {
                JsonNode firstRoute = routes.get(0);
                double distance = firstRoute.path("distance").asDouble(); // distance in metres
                double duration = firstRoute.path("duration").asDouble(); // duration in seconds
                
                return new DistanceResult(distance, duration);
            } else {
                throw new OsrmException("No valid route found in the JSON response", OsrmException.ErrorType.INVALID_RESPONSE);
            }

        } catch (java.net.SocketTimeoutException e) {
            throw new OsrmException("OSRM connection timed out", OsrmException.ErrorType.TIMEOUT, e);
        } catch (OsrmServiceException e) {
            throw e; 
        } catch (Exception e) {
            // General catch-all for IO issues or parsing failures
            throw new OsrmServiceException("Could not connect to OSRM service: " + e.getMessage(), e);
        }
    }
}