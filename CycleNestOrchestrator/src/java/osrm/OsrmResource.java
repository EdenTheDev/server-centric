package osrm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/distance")
public class OsrmResource {

    private static final Logger LOGGER = Logger.getLogger(OsrmResource.class.getName());
    private final OsrmClient client = new OsrmClient();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDistance(
            @QueryParam("lat1") Double lat1, // Using Double (Object) to detect nulls
            @QueryParam("lon1") Double lon1,
            @QueryParam("lat2") Double lat2,
            @QueryParam("lon2") Double lon2) {

        // 1. Validation: If params are missing or zero (default for primitive double)
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Invalid parameters. Please provide lat1, lon1, lat2, and lon2.\"}")
                    .build();
        }

        try {
            DistanceResult result = client.calculateDistance(lat1, lon1, lat2, lon2);
            return Response.ok(result).build();

        } catch (OsrmException e) {
            // 2. Specific Exception: The OSRM server failed or coordinates were invalid
            LOGGER.warning("OSRM Service issue: " + e.getMessage());
            return Response.status(Response.Status.BAD_GATEWAY)
                    .entity("{\"error\":\"OSRM service unavailable or returned an error\"}")
                    .build();
            
        } catch (Exception e) {
            // 3. General Fallback: Internal system error
            LOGGER.severe("Unexpected Orchestrator error: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An internal system error occurred\"}")
                    .build();
        }
    }
}