package cyclenest.resource;

import cyclenest.model.Item;
import cyclenest.repository.ItemRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * REST resource for managing cycle rental items.
 */
@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    private final ItemRepository repository = new ItemRepository();

    /**
     * GET /items
     * Returns all available items.
     */
    @GET
    public Collection<Item> getAllItems() {
        return repository.getAllItems();
    }

    /**
     * GET /items/{id}
     * Returns a single item by ID.
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
     * Adds a new item.
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
     * Updates an existing item.
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
     * Removes an item.
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
