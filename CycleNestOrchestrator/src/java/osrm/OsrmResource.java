package osrm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/distance")
public class OsrmResource {

    private final OsrmClient client = new OsrmClient();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDistance(
            @QueryParam("lat1") double lat1,
            @QueryParam("lon1") double lon1,
            @QueryParam("lat2") double lat2,
            @QueryParam("lon2") double lon2) {

        try {
            DistanceResult result =
                    client.calculateDistance(lat1, lon1, lat2, lon2);

            return Response.ok(result).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_GATEWAY)
                    .entity("{\"error\":\"OSRM service unavailable\"}")
                    .build();
        }
    }
}
