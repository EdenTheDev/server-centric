package cyclenest.model;

public class Item {
    private int id;
    private String name;
    private String type;
    private String location;
    private boolean available;
    private double latitude;
    private double longitude;
    private double roughDistance; // The missing symbol

    public Item() {}

    public Item(int id, String name, String type, String location, boolean available, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.available = available;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Standard Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    // THE METHODS CAUSING THE ERROR - ENSURE THESE ARE EXACT
    public double getRoughDistance() {
        return roughDistance;
    }
public boolean isAvailable() {
    return available;
}

public void setAvailable(boolean available) {
    this.available = available;
}
    public void setRoughDistance(double roughDistance) {
        this.roughDistance = roughDistance;
    }
}