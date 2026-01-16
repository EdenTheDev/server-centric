package cyclenest.model;

public class RentalRequest {
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_CANCELLED = "cancelled";

    private int requestId;
    private String itemId; // Changed from int to String
    private String startDate;
    private String endDate;
    private String status;

    public RentalRequest() {}

    public RentalRequest(int requestId, String itemId, String startDate, String endDate, String status) {
        this.requestId = requestId;
        this.itemId = itemId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = (status == null || status.isEmpty()) ? STATUS_PENDING : status;
    }

    // Standard Getters/Setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public String getItemId() { return itemId; } // Changed to String
    public void setItemId(String itemId) { this.itemId = itemId; } // Changed to String

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}