package cyclenest.resource;

import cyclenest.model.Item;
import cyclenest.model.RentalRequest;
import cyclenest.repository.ItemRepository;
import cyclenest.osrm.OsrmClient;
import cyclenest.osrm.DistanceResult;
import cyclenest.osrm.OsrmException;
import cyclenest.osrm.OsrmServiceException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * ItemResource - Central API Controller for CycleNest.
 * Handles the orchestration between MongoDB persistence and OSRM routing.
 */
@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    private final ItemRepository repository = new ItemRepository();
    private final OsrmClient osrmClient = new OsrmClient();

    /**
     * Retrieves all items from the repository.
     */
    @GET
    public Response getItems() {
        return Response.ok(repository.getAllItems()).build();
    }

    /**
     * Utility search endpoint supporting various filters.
     * Maps directly to the MongoDB search implementation in the Repository.
     */
    @GET
    @Path("/search")
    public Response searchItems(
            @QueryParam("item_id") String itemId,
            @QueryParam("owner_id") String ownerId,
            @QueryParam("name") String name,
            @QueryParam("category") String category,
            @QueryParam("available") Boolean available,
            @QueryParam("maxRate") Double maxRate,
            @QueryParam("location") String location,
            @QueryParam("condition") String condition) {
        
        List<Item> results = repository.searchItems(
            itemId, ownerId, name, category, available, maxRate, location, condition
        );
        return Response.ok(results).build();
    }

    /**
     * Retrieves a single item by its unique ID.
     */
    @GET
    @Path("/{id}")
    public Response getItemById(@PathParam("id") String id) { 
        Item item = repository.getItemById(id);
        if (item == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(item).build();
    }

    /**
     * Orchestration Endpoint: Calculates road distance between a user and a specific cycle.
     * Demonstrates Part B requirement for inter-service communication.
     */
    @GET
    @Path("/{id}/distance")
    public Response getDistanceToItem(
            @PathParam("id") String id, 
            @QueryParam("userLat") double userLat, 
            @QueryParam("userLon") double userLon) {
        
        // Fetch cycle details from MongoDB Atlas
        Item item = repository.getItemById(id);
        
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Item not found in database\"}").build();
        }

        try {
            // Orchestrate call to the OSRM Docker container
            DistanceResult result = osrmClient.getRoute(
                userLat, userLon, 
                item.getLatitude(), item.getLongitude()
            );
            return Response.ok(result).build();

        } catch (OsrmServiceException e) {
            // Specific handling if the OSRM microservice is offline
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                           .entity("{\"error\":\"Routing service is currently unavailable\"}").build();
        } catch (OsrmException e) {
            // Handling for invalid routing data or timeouts
            return Response.status(Response.Status.BAD_GATEWAY)
                           .entity("{\"error\":\"Failed to calculate route: " + e.getMessage() + "\"}").build();
        }
    }

    /**
     * Adds a new item to the repository.
     */
    @POST
    public Response addItem(Item item) {
        if (item == null) return Response.status(Response.Status.BAD_REQUEST).build();
        repository.addItem(item); 
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    /**
     * State Management: Handles the 'pending' status for new rental requests.
     */
    @POST
    @Path("/request")
    public Response requestItem(RentalRequest request) {
        if (request == null || request.getItemId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"Invalid request data\"}").build();
        }
        
        request.setStatus("pending");
        repository.saveRentalRequest(request); 
        return Response.status(Response.Status.CREATED).entity(request).build();
    }

    /**
     * State Management: Cancellation logic for existing requests.
     */
    @PUT
    @Path("/request/{requestId}/cancel")
    public Response cancelRequest(@PathParam("requestId") String requestId) {
        boolean updated = repository.updateRequestStatus(requestId, "cancelled");
        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Request ID not found\"}").build();
        }
        return Response.ok("{\"message\":\"Request successfully cancelled\"}").build();
    }

    /**
     * Deletes an item from the repository by its ID.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") String id) { 
        Item removed = repository.removeItem(id);
        if (removed == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.noContent().build();
    }
}