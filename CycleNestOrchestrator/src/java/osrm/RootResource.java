package osrm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/") // This handles the base URL
public class RootResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatus() {
        return "{\"status\":\"Online\", \"service\":\"CycleNest Orchestrator\", \"version\":\"1.0\"}";
    }
}