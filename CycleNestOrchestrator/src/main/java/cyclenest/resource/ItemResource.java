package cyclenest.resource;

import cyclenest.model.Item;
import cyclenest.model.RentalRequest; // You will need to create this model
import cyclenest.repository.ItemRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    private final ItemRepository repository = new ItemRepository();

    // GET /api/items/search -> Supports Category, Availability, MaxRate, and Location
    @GET
    @Path("/search")
    public Response searchItems(
            @QueryParam("category") String category,
            @QueryParam("available") Boolean available,
            @QueryParam("maxRate") Double maxRate,
            @QueryParam("location") String location) {
        
        // Ensure your repository.searchItems can handle 'location' now
        List<Item> filteredResults = repository.searchItems(category, available, maxRate, location);
        return Response.ok(filteredResults).build();
    }

    // Requirement: Request an item (Creates a "pending" request)
    // POST /api/items/request
    @POST
    @Path("/request")
    public Response requestItem(RentalRequest request) {
        if (request == null || request.getItemId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Request Data").build();
        }
        
        // Business Logic: Status must be "pending" initially
        request.setStatus("pending");
        
        repository.saveRentalRequest(request); 
        return Response.status(Response.Status.CREATED).entity(request).build();
    }

    // Requirement: Cancel a request (Updates status to "cancelled")
    // PUT /api/items/request/{requestId}/cancel
    @PUT
    @Path("/request/{requestId}/cancel")
    public Response cancelRequest(@PathParam("requestId") String requestId) {
        boolean updated = repository.updateRequestStatus(requestId, "cancelled");
        
        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND).entity("Request not found").build();
        }
        return Response.ok("{\"message\":\"Request cancelled successfully\"}").build();
    }

    // --- YOUR EXISTING CRUD METHODS ---

    @GET
    public Response getItems() {
        return Response.ok(repository.getAllItems()).build();
    }

    @GET
    @Path("/{id}")
    public Response getItemById(@PathParam("id") String id) { 
        Item item = repository.getItemById(id);
        if (item == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(item).build();
    }

    @POST
    public Response addItem(Item item) {
        if (item == null) return Response.status(Response.Status.BAD_REQUEST).build();
        repository.addItem(item); 
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") String id) { 
        Item removed = repository.removeItem(id);
        if (removed == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.noContent().build();
    }
}