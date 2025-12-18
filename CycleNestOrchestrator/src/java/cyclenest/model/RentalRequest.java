package cyclenest.model;

/**
 * Represents a rental request for a cycle item.
 */
public class RentalRequest {

    private int requestId;
    private int itemId;
    private String startDate;
    private String endDate;
    private String status;

    public RentalRequest() {
        // Required for JSON deserialization
    }

    public RentalRequest(int requestId, int itemId, String startDate, String endDate, String status) {
        this.requestId = requestId;
        this.itemId = itemId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
