package cyclenest.model;

public class Item {

    private int id;
    private String name;
    private String type;
    private String location;
    private boolean available;

    public Item() {
        // Required for JSON deserialization
    }

    public Item(int id, String name, String type, String location, boolean available) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
