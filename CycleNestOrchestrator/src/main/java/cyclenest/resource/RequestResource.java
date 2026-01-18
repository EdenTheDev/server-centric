package cyclenest.resource;

import cyclenest.model.RentalRequest;
import cyclenest.repository.RequestRepository;
import cyclenest.repository.ItemRepository;
import cyclenest.model.Item; // Make sure to import Item

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/requests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RequestResource {

    private final RequestRepository requestRepo = new RequestRepository();
    private final ItemRepository itemRepo = new ItemRepository();

    @GET
    public Collection<RentalRequest> getAllRequests() {
        return requestRepo.getAllRequests();
    }

    @GET
    @Path("{id}")
    public Response getRequestById(@PathParam("id") int id) {
        RentalRequest request = requestRepo.getRequestById(id);
        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Request not found\"}")
                    .build();
        }
        return Response.ok(request).build();
    }

    @POST
    public Response createRequest(RentalRequest request) {
        Item targetItem = itemRepo.getItemById(request.getItemId());

        if (targetItem == null || !targetItem.isAvailable()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Item not available or not found\"}")
                    .build();
        }

        // Force status to "pending" on creation
        request.setStatus("pending");

        RentalRequest created = requestRepo.addRequest(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}/cancel")
    public Response cancelRequest(@PathParam("id") int id) {
        RentalRequest request = requestRepo.getRequestById(id);

        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Request " + id + " does not exist\"}")
                    .build();
        }

        request.setStatus(RentalRequest.STATUS_CANCELLED);
        requestRepo.updateRequest(id, request);
        return Response.ok(request).build();
    }
}