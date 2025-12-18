package cyclenest.repository;

import cyclenest.model.Item;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory repository for managing cycle items.
 * This repository simulates persistence for Part A.
 */
public class ItemRepository {

    private static final Map<Integer, Item> items = new HashMap<>();

    // Static initial data (optional but useful for testing)
    static {
        items.put(1, new Item(1, "City Bike", "Urban", "Nottingham", true));
        items.put(2, new Item(2, "Mountain Bike", "Off-road", "Derby", true));
        items.put(3, new Item(3, "Road Bike", "Sport", "Leicester", false));
    }

    public Collection<Item> getAllItems() {
        return items.values();
    }

    public Item getItemById(int id) {
        return items.get(id);
    }

    public Item addItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public Item updateItem(int id, Item updatedItem) {
        items.put(id, updatedItem);
        return updatedItem;
    }

    public Item removeItem(int id) {
        return items.remove(id);
    }
}
