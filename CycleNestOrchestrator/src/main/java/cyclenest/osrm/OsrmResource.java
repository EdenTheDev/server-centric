package cyclenest.osrm;

import cyclenest.model.Item;
import cyclenest.repository.ItemRepository;
import cyclenest.util.DistanceHelper;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("/distance")
public class OsrmResource {

    private static final Logger LOGGER = Logger.getLogger(OsrmResource.class.getName());
    private final OsrmClient client = new OsrmClient();
    private final ItemRepository itemRepo = new ItemRepository(); 

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClosestBike(
            @QueryParam("lat") Double userLat, 
            @QueryParam("lon") Double userLon) {

        // 1. Validation
        if (userLat == null || userLon == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Please provide your current lat and lon.\"}")
                    .build();
        }

       try {
            // 2. Load a paginated sample from Atlas (Optimized for Part C)
            // Added 1 (page) and 10 (pageSize) to match new ItemRepository signature
            List<Item> allItems = itemRepo.searchItems(null, null, null, null, null, null, null, null, 1, 10);

            if (allItems == null || allItems.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No bikes found in database to calculate distance.\"}")
                        .build();
            }

            // 3. KNN FILTERING: Local math to prevent 429 Errors
            for (Item item : allItems) {
                double roughDist = DistanceHelper.calculateHaversine(
                    userLat, userLon, item.getLatitude(), item.getLongitude());
                item.setRoughDistance(roughDist);
            }
            
            // 4. SORT AND LIMIT: Keep only the 3 closest items locally
            List<Item> top3Bikes = allItems.stream()
                    .sorted(Comparator.comparingDouble(Item::getRoughDistance))
                    .limit(3)
                    .collect(Collectors.toList());

            // 5. TARGETED ORCHESTRATION: Only call OSRM for the absolute closest bike
            Item closestBike = top3Bikes.get(0);
            DistanceResult finalResult = client.calculateDistance(
                userLat, userLon, closestBike.getLatitude(), closestBike.getLongitude());

            return Response.ok(finalResult).build();

        } catch (OsrmException e) {
            LOGGER.warning("OSRM Service issue: " + e.getMessage());
            return Response.status(Response.Status.BAD_GATEWAY)
                    .entity("{\"error\":\"External routing service failed (Rate Limit or Timeout)\"}")
                    .build();
        } catch (Exception e) {
            LOGGER.severe("Unexpected Orchestrator error: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Internal error during orchestration\"}")
                    .build();
        }
    }

    @GET
    @Path("/direct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDirectDistance(
            @QueryParam("lat1") Double lat1, @QueryParam("lon1") Double lon1,
            @QueryParam("lat2") Double lat2, @QueryParam("lon2") Double lon2) {
        try {
            // This bypasses the Repository/Database entirely for clean OSRM testing
            DistanceResult result = client.calculateDistance(lat1, lon1, lat2, lon2);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }
}