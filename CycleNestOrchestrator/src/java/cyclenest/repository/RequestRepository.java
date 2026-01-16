package cyclenest.repository;

import cyclenest.model.RentalRequest;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestRepository {
    private static final Map<Integer, RentalRequest> requests = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    public Collection<RentalRequest> getAllRequests() {
        return requests.values();
    }

    public RentalRequest getRequestById(int id) {
        return requests.get(id);
    }

    public RentalRequest addRequest(RentalRequest request) {
        if (request.getRequestId() <= 0) {
            request.setRequestId(idGenerator.incrementAndGet());
        }
        if (request.getStatus() == null) {
            request.setStatus(RentalRequest.STATUS_PENDING);
        }
        requests.put(request.getRequestId(), request);
        return request;
    }

    // This method is what the Resource is looking for!
    public void updateRequest(int id, RentalRequest updatedRequest) {
        requests.put(id, updatedRequest);
    }
}