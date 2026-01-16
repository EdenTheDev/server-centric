package cyclenest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Item model updated for MongoDB Atlas (String IDs) and OSRM calculations.
 */
@JsonIgnoreProperties(ignoreUnknown = true) 
public class Item {
    
    private String item_id; 
    private String owner_id; 
    private String name;
    private String category;
    private String location;    
    private double daily_rate; 
    private boolean available;  
    private String condition;   
    private String description; 

    private double latitude;
    private double longitude;
    private double roughDistance; 

    // 1. MUST HAVE: Default constructor for Jackson
    public Item() {
    }

    // 2. Updated constructor
    public Item(String item_id, String owner_id, String name, String category, String location, 
                double daily_rate, boolean available, String condition, 
                String description, double latitude, double longitude) {
        this.item_id = item_id;
        this.owner_id = owner_id;
        this.name = name;
        this.category = category;
        this.location = location;
        this.daily_rate = daily_rate;
        this.available = available;
        this.condition = condition;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // 3. GETTERS AND SETTERS (Fixed to match field names exactly)

    @JsonProperty("item_id")
    public String getItem_id() { return item_id; }
    public void setItem_id(String item_id) { this.item_id = item_id; }

    @JsonProperty("owner_id")
    public String getOwner_id() { return owner_id; }
    public void setOwner_id(String owner_id) { this.owner_id = owner_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @JsonProperty("daily_rate")
    public double getDaily_rate() { return daily_rate; }
    public void setDaily_rate(double daily_rate) { this.daily_rate = daily_rate; }

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