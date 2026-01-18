package cyclenest.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import cyclenest.model.Item;
import cyclenest.model.RentalRequest;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    private static final String CONNECTION_STRING = "mongodb+srv://n1085361:CycleNest123@cyclenestcluster.9ibdwx0.mongodb.net/?appName=CycleNestCluster";
    private static final String DATABASE_NAME = "CycleNestDB";
    private static final String ITEM_COLLECTION = "items";
    private static final String REQUEST_COLLECTION = "requests";

    // SINGLETON FIX: One client, shared by all requests
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    public ItemRepository() {
        synchronized (ItemRepository.class) {
            if (mongoClient == null) {
                mongoClient = MongoClients.create(CONNECTION_STRING);
                database = mongoClient.getDatabase(DATABASE_NAME);
            }
        }
    }

    private MongoCollection<Document> getItemCollection() {
        return database.getCollection(ITEM_COLLECTION);
    }

    private MongoCollection<Document> getRequestCollection() {
        return database.getCollection(REQUEST_COLLECTION);
    }

    /**
     * OPTIMIZED SEARCH: Added Page and PageSize for Pagination (Part C)
     */
    public List<Item> searchItems(String itemId, String ownerId, String name, String category, 
                                  Boolean available, Double maxRate, String location, String condition,
                                  int page, int pageSize) {
        List<Item> itemList = new ArrayList<>();
        Document query = new Document();
        
        if (itemId != null && !itemId.isEmpty()) query.append("item_id", itemId);
        if (ownerId != null && !ownerId.isEmpty()) query.append("owner_id", ownerId);
        if (available != null) query.append("available", available);
        
        if (name != null && !name.isEmpty()) 
            query.append("name", new Document("$regex", "(?i)" + name));
        if (category != null && !category.isEmpty()) 
            query.append("category", new Document("$regex", "(?i)" + category));
        if (location != null && !location.isEmpty()) 
            query.append("location", new Document("$regex", "(?i)" + location));
        if (condition != null && !condition.isEmpty()) 
            query.append("condition", new Document("$regex", "(?i)" + condition));
            
        if (maxRate != null) query.append("daily_rate", new Document("$lte", maxRate));

        // PAGINATION EXECUTION: skip() and limit()
        int skipValue = (page - 1) * pageSize;

        for (Document doc : getItemCollection().find(query)
                                               .skip(skipValue)
                                               .limit(pageSize)) {
            itemList.add(mapDocumentToItem(doc));
        }
        return itemList;
    }

    // Overloaded method to maintain compatibility with existing code if needed
    public List<Item> searchItems(String itemId, String ownerId, String name, String category, 
                                  Boolean available, Double maxRate, String location, String condition) {
        return searchItems(itemId, ownerId, name, category, available, maxRate, location, condition, 1, 50);
    }

    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        // Default to first 50 items
        for (Document doc : getItemCollection().find().limit(50)) {
            itemList.add(mapDocumentToItem(doc));
        }
        return itemList;
    }

    public Item getItemById(String id) {
        Document doc = getItemCollection().find(Filters.eq("item_id", id)).first();
        return (doc != null) ? mapDocumentToItem(doc) : null;
    }

    public void saveRentalRequest(RentalRequest request) {
        Document doc = new Document("requestId", request.getRequestId())
                .append("itemId", request.getItemId())
                .append("startDate", request.getStartDate())
                .append("endDate", request.getEndDate())
                .append("status", RentalRequest.STATUS_PENDING); 
        
        getRequestCollection().insertOne(doc);
    }

    public boolean updateRequestStatus(String requestId, String newStatus) {
        try {
            Bson filter = Filters.eq("requestId", Integer.parseInt(requestId));
            Bson update = Updates.set("status", newStatus);
            return getRequestCollection().updateOne(filter, update).getModifiedCount() > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void addItem(Item item) {
        Document doc = new Document("item_id", item.getItem_id())
                .append("name", item.getName())
                .append("category", item.getCategory())
                .append("daily_rate", item.getDaily_rate())
                .append("available", item.isAvailable())
                .append("latitude", item.getLatitude())
                .append("longitude", item.getLongitude());
        getItemCollection().insertOne(doc);
    }

    public Item removeItem(String id) {
        Item item = getItemById(id);
        if (item != null) {
            getItemCollection().deleteOne(Filters.eq("item_id", id));
        }
        return item;
    }

    private Item mapDocumentToItem(Document doc) {
        return new Item(
            doc.getString("item_id"),
            doc.getString("owner_id"),
            doc.getString("name"),
            doc.getString("category"),
            doc.getString("location"),
            doc.get("daily_rate") instanceof Number ? ((Number) doc.get("daily_rate")).doubleValue() : 0.0,
            doc.getBoolean("available") != null ? doc.getBoolean("available") : false,
            doc.getString("condition"),
            doc.getString("description"),
            doc.get("latitude") instanceof Number ? ((Number) doc.get("latitude")).doubleValue() : 0.0,
            doc.get("longitude") instanceof Number ? ((Number) doc.get("longitude")).doubleValue() : 0.0
        );
    }
}