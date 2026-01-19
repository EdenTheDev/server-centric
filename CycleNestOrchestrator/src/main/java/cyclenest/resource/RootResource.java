package cyclenest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * RootResource - The entry point for the API.
 * Provides health checks to ensure the Tomcat server and Docker network are behaving.
 */
@Path("/") 
public class RootResource {

    /**
     * Base endpoint (/api)
     * Returns a simple JSON string to verify the listener is active.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatus() {
        // Simple hardcoded JSON for a quick connectivity test
        return "{\"status\":\"Online\", \"service\":\"CycleNest Orchestrator\", \"version\":\"1.0\"}";
    }

    /**
     * Detailed status endpoint (/api/status)
     * Using LinkedHashMap to keep the JSON output organised and readable.
     */
    @GET
    @Path("/status") 
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getSystemStatus() {
        // LinkedHashMap keeps the keys in the order they are added
        Map<String, Object> response = new LinkedHashMap<>();
        
        response.put("status", "Online");
        response.put("service", "CycleNest Orchestrator");
        response.put("version", "1.0");
        
        // This confirms the OSRM container is part of the same Docker network
        response.put("docker_osrm", "Connected");
        
        return response;
    }
}