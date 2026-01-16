package cyclenest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/") 
public class RootResource {

    // This handles /api
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatus() {
        return "{\"status\":\"Online\", \"service\":\"CycleNest Orchestrator\", \"version\":\"1.0\"}";
    }

    // This handles /api/status
    @GET
    @Path("/status") 
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "Online");
        response.put("service", "CycleNest Orchestrator");
        response.put("version", "1.0");
        response.put("docker_osrm", "Connected");
        return response;
    }
}