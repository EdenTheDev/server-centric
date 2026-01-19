package cyclenest.osrm;

import cyclenest.model.Item;
import cyclenest.repository.ItemRepository;
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

/**
 * OsrmResource - The main orchestrator for distance-based logic.
 * This class handles finding the nearest bike by combining local math 
 * with the external OSRM routing engine.
 */
@Path("/distance")
public class OsrmResource {

    private static final Logger LOGGER = Logger.getLogger(OsrmResource.class.getName());
    private final OsrmClient osrmClient = new OsrmClient();
    private final ItemRepository itemRepo = new ItemRepository(); 

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClosestBike(
            @QueryParam("lat") Double userLat, 
            @QueryParam("lon") Double userLon) {

        // Basic validation to ensure the user actually sent coordinates
        if (userLat == null || userLon == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Please provide your current latitude and longitude.\"}")
                    .build();
        }

        try {
            // Fetch all items from MongoDB Atlas
            List<Item> allItems = itemRepo.searchItems(null, null, null, null, null, null, null, null);

            if (allItems == null || allItems.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No bikes found in the database.\"}")
                        .build();
            }

            /* * Efficiency Optimisation:
             * We use the Haversine formula (local math) to find the closest bike first.
             * This prevents us from making 100s of expensive external OSRM API calls.
             */
            for (Item item : allItems) {
                if (item.getLatitude() == 0 || item.getLongitude() == 0) continue;

                double roughDist = calculateHaversine(
                    userLat, userLon, item.getLatitude(), item.getLongitude());
                item.setRoughDistance(roughDist);
            }
            
            // Sort bikes by "as the crow flies" distance and pick the top candidate
            List<Item> sortedBikes = allItems.stream()
                    .filter(i -> i.getRoughDistance() > 0)
                    .sorted(Comparator.comparingDouble(Item::getRoughDistance))
                    .collect(Collectors.toList());

            if (sortedBikes.isEmpty()) {
                 return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No items with valid coordinates found.\"}")
                        .build();
            }

            // Now that we have the best candidate, we use OSRM for precise road-routing distance
            Item closestBike = sortedBikes.get(0);
            
            DistanceResult finalResult = osrmClient.getRoute(
                userLat, userLon, closestBike.getLatitude(), closestBike.getLongitude());

            return Response.ok(finalResult).build();

        } catch (OsrmServiceException e) {
            // Error handling for when the Docker OSRM service is down or unreachable
            LOGGER.warning("OSRM Service connection failure: " + e.getMessage());
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("{\"error\":\"Routing engine is offline. " + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            LOGGER.severe("Orchestrator encountered an unexpected error: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An internal error occurred during orchestration\"}")
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
            DistanceResult result = osrmClient.getRoute(lat1, lon1, lat2, lon2);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Haversine formula - calculates "as the crow flies" distance in metres.
     * Used for the initial KNN (K-Nearest Neighbour) filter.
     */
    private double calculateHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c * 1000; // Convert km to metres
    }
}