package cyclenest.repository;

import cyclenest.model.Item;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe in-memory repository for cycle items.
 * Currently using hardcoded toy data for Part A testing.
 */
public class ItemRepository {

    private static final Map<Integer, Item> items = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(3);

    static {
        // Updated to match the 11-parameter constructor:
        // ID, OwnerID, Name, Category, Location, DailyRate, Availability, Condition, Description, Lat, Lon
        
        items.put(1, new Item(
            1, 101, "City Bike", "Urban", "Nottingham", 
            12.50, true, "Good", "Standard bike for city commuting.", 
            52.9548, -1.1581
        ));

        items.put(2, new Item(
            2, 102, "Mountain Bike", "Off-road", "Derby", 
            25.00, true, "New", "Tough bike for mountain trails.", 
            52.9225, -1.4746
        ));

        items.put(3, new Item(
            3, 101, "Road Bike", "Sport", "Leicester", 
            20.00, false, "Fair", "Fast bike for long distance road cycling.", 
            52.6369, -1.1398
        ));
    }

    public Collection<Item> getAllItems() {
        return items.values();
    }

    public Item getItemById(int id) {
        return items.get(id);
    }

    public Item addItem(Item item) {
        if (item.getId() <= 0) {
            item.setId(idGenerator.incrementAndGet());
        }
        items.put(item.getId(), item);
        return item;
    }

    // This stays the same for now, but will eventually need to update the Cloud DB
    public Item updateItem(int id, Item updatedItem) {
        updatedItem.setId(id);
        items.put(id, updatedItem);
        return updatedItem;
    }

    public Item removeItem(int id) {
        return items.remove(id);
    }
}