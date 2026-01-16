package cyclenest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Item model updated to match the 9-attribute requirement in Section A.2.
 * Also keeps lat/lon for the OSRM distance calculations in Part B.
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Stops the app from crashing if the 10k JSON has extra fields
public class Item {
    
    // Mandatory spec fields
    private int id;
    private int ownerId;        // Spec requirement
    private String name;
    private String category;    // Spec calls it Category (renamed from 'type')
    private String location;    
    private double dailyRate;   // Spec requirement
    private boolean available;  // Spec requirement
    private String condition;   // Spec requirement
    private String description; // Spec requirement

    // Fields needed for Part B (Proximity/OSRM)
    private double latitude;
    private double longitude;
    private double roughDistance; 

    public Item() {
        // Default constructor for Jackson
    }

    // Updated constructor with the new spec fields
    public Item(int id, int ownerId, String name, String category, String location, 
                double dailyRate, boolean available, String condition, 
                String description, double latitude, double longitude) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.category = category;
        this.location = location;
        this.dailyRate = dailyRate;
        this.available = available;
        this.condition = condition;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getRoughDistance() { return roughDistance; }
    public void setRoughDistance(double roughDistance) { this.roughDistance = roughDistance; }
}