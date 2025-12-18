package cyclenest.repository;

import cyclenest.model.RentalRequest;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe in-memory repository for managing rental requests.
 */
public class RequestRepository {

    // Thread-safe storage for requests
    private static final Map<Integer, RentalRequest> requests = new ConcurrentHashMap<>();

    // Thread-safe request ID generation
    private static final AtomicInteger requestIdGenerator = new AtomicInteger(0);

    /**
     * Retrieve all rental requests.
     */
    public Collection<RentalRequest> getAllRequests() {
        return requests.values();
    }

    /**
     * Retrieve a rental request by ID.
     */
    public RentalRequest getRequestById(int id) {
        return requests.get(id);
    }

    /**
     * Create a new rental request.
     */
   public RentalRequest addRequest(RentalRequest request) {
    request.setRequestId(requestIdGenerator.incrementAndGet());
    request.setStatus("PENDING");
    requests.put(request.getRequestId(), request);
    return request;
    }
}
