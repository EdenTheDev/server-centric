package cyclenest.repository;

import cyclenest.model.Item;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe in-memory repository for managing cycle rental items.
 * Supports concurrent access and server-side ID generation.
 */
public class ItemRepository {

    private static final Map<Integer, Item> items = new ConcurrentHashMap<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(3);

    static {
        // Updated to match the new 7-parameter constructor: 
        // ID, Name, Type, Location, Availability, Latitude, Longitude
        items.put(1, new Item(1, "City Bike", "Urban", "Nottingham", true, 52.9548, -1.1581));
        items.put(2, new Item(2, "Mountain Bike", "Off-road", "Derby", true, 52.9225, -1.4746));
        items.put(3, new Item(3, "Road Bike", "Sport", "Leicester", false, 52.6369, -1.1398));
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

    public Item updateItem(int id, Item updatedItem) {
        updatedItem.setId(id);
        items.put(id, updatedItem);
        return updatedItem;
    }

    public Item removeItem(int id) {
        return items.remove(id);
    }
}