package osrm;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/distance")
@Produces(MediaType.APPLICATION_JSON)
public class OsrmResource {

    private final OsrmClient client = new OsrmClient();

    @GET
    public DistanceResult getDistance(
            @QueryParam("lat1") double lat1,
            @QueryParam("lon1") double lon1,
            @QueryParam("lat2") double lat2,
            @QueryParam("lon2") double lon2) throws Exception {
        return client.calculateDistance(lat1, lon1, lat2, lon2);
    }
}
