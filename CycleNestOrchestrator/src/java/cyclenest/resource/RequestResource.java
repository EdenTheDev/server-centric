package cyclenest.resource;

import cyclenest.model.RentalRequest;
import cyclenest.repository.RequestRepository;
import cyclenest.repository.ItemRepository;

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

    // GET /requests
    @GET
    public Collection<RentalRequest> getAllRequests() {
        return requestRepo.getAllRequests();
    }

    // GET /requests/{id}
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

    // POST /requests
    @POST
    public Response createRequest(RentalRequest request) {

        if (itemRepo.getItemById(request.getItemId()) == null ||
            !itemRepo.getItemById(request.getItemId()).isAvailable()) {

            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Item not available\"}")
                    .build();
        }

        RentalRequest created = requestRepo.addRequest(request);

        return Response.status(Response.Status.CREATED)
                .entity(created)
                .build();
    }
}
