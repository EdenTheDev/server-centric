package cyclenest.resource;

import cyclenest.model.Item;
import cyclenest.repository.ItemRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST resource for managing the Cycle Nest item marketplace.
 * Updated to support Section A.1 requirements: Search and Filter.
 */
@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    private final ItemRepository repository = new ItemRepository();

    /**
     * GET /items
     * Supports filtering by category, availability, and daily rate.
     * This ensures the Orchestrator can handle specific user queries 
     * across the 10,000 item dataset.
     */
    @GET
    public Response getItems(
            @QueryParam("category") String category,
            @QueryParam("available") Boolean available,
            @QueryParam("maxRate") Double maxRate) {
        
        // Fetching all items from repository (currently in-memory, will be Cosmos DB)
        Collection<Item> items = repository.getAllItems();
        
        // Using Java Streams for filtering - helpful for the search requirement
        List<Item> filteredResults = items.stream()
            .filter(i -> category == null || i.getCategory().equalsIgnoreCase(category))
            .filter(i -> available == null || i.isAvailable() == available)
            .filter(i -> maxRate == null || i.getDailyRate() <= maxRate)
            .collect(Collectors.toList());

        return Response.ok(filteredResults).build();
    }

    /**
     * GET /items/{id}
     * Returns a single item by ID for detailed view.
     */
    @GET
    @Path("/{id}")
    public Response getItemById(@PathParam("id") int id) {
        Item item = repository.getItemById(id);

        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Item not found\"}")
                    .build();
        }

        return Response.ok(item).build();
    }

    /**
     * POST /items
     * Adds a new item to the marketplace.
     */
    @POST
    public Response addItem(Item item) {
        if (item == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Invalid item payload\"}")
                    .build();
        }

        repository.addItem(item);
        return Response.status(Response.Status.CREATED)
                .entity(item)
                .build();
    }

    /**
     * PUT /items/{id}
     * Updates an existing item (e.g., changing availability or description).
     */
    @PUT
    @Path("/{id}")
    public Response updateItem(@PathParam("id") int id, Item item) {
        if (repository.getItemById(id) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Item not found\"}")
                    .build();
        }

        item.setId(id);
        repository.updateItem(id, item);
        return Response.ok(item).build();
    }

    /**
     * DELETE /items/{id}
     * Admin/Owner functionality to remove an item.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") int id) {
        Item removed = repository.removeItem(id);

        if (removed == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Item not found\"}")
                    .build();
        }

        return Response.noContent().build();
    }
}