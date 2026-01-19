package cyclenest.model;

/**
 * RentalRequest - Model for handling cycle bookings.
 * This links back to the "State Management" requirement in Part A.1.
 */
public class RentalRequest {
    // Constant status types to avoid typos throughout the app
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_CANCELLED = "cancelled";

    private int requestId;
    private String itemId;    // String ID to match MongoDB's _id format
    private String startDate;
    private String endDate;
    private String status;

    // Default constructor for Jackson JSON parsing
    public RentalRequest() {}

    public RentalRequest(int requestId, String itemId, String startDate, String endDate, String status) {
        this.requestId = requestId;
        this.itemId = itemId;
        this.startDate = startDate;
        this.endDate = endDate;
        // Defaulting to "pending" if no status is provided
        this.status = (status == null || status.isEmpty()) ? STATUS_PENDING : status;
    }

    // Getters and Setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public String getItemId() { return itemId; } 
    public void setItemId(String itemId) { this.itemId = itemId; } 

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}