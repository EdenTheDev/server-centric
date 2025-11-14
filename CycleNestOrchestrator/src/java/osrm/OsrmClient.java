package osrm;

import java.net.http.*;
import java.net.*;
import java.time.Duration;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OsrmClient {

    private static final String URL =
        "https://router.project-osrm.org/table/v1/driving/%f,%f;%f,%f?annotations=distance,duration";

    public DistanceResult calculateDistance(double lat1, double lon1,
                                            double lat2, double lon2) throws Exception {

        String apiUrl = String.format(URL, lon1, lat1, lon2, lat2);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("OSRM service unavailable");
        }

        ObjectMapper mapper = new ObjectMapper();
        OsrmResponse osrm;
        osrm = mapper.readValue(response.body(), OsrmResponse.class);

        double distance = osrm.distances[0][1];
        double duration = osrm.durations[0][1];

        return new DistanceResult(distance, duration);
    }
}
