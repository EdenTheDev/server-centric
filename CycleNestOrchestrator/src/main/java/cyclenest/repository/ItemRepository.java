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

    private MongoDatabase getDatabase() {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    private MongoCollection<Document> getItemCollection() {
        return getDatabase().getCollection(ITEM_COLLECTION);
    }

    private MongoCollection<Document> getRequestCollection() {
        return getDatabase().getCollection(REQUEST_COLLECTION);
    }

    // A.1 Requirement: Search by at least one criterion (Category, Availability, Rate, and LOCATION)
    public List<Item> searchItems(String category, Boolean available, Double maxRate, String location) {
        List<Item> itemList = new ArrayList<>();
        Document query = new Document();
        
        if (category != null && !category.isEmpty()) query.append("category", category);
        if (available != null) query.append("available", available);
        if (maxRate != null) query.append("daily_rate", new Document("$lte", maxRate));
        if (location != null && !location.isEmpty()) query.append("location", location);

        // Limit to 50 for performance as you did before
        for (Document doc : getItemCollection().find(query).limit(50)) {
            itemList.add(mapDocumentToItem(doc));
        }
        return itemList;
    }

    // A.1 Requirement: Request an item (Status: Pending)
    public void saveRentalRequest(RentalRequest request) {
        Document doc = new Document("requestId", request.getRequestId())
                .append("itemId", request.getItemId())
                .append("startDate", request.getStartDate())
                .append("endDate", request.getEndDate())
                .append("status", RentalRequest.STATUS_PENDING); // Force pending status
        
        getRequestCollection().insertOne(doc);
    }

    // A.1 Requirement: Cancel a request (Update status to Cancelled)
    public boolean updateRequestStatus(String requestId, String newStatus) {
        Bson filter = Filters.eq("requestId", Integer.parseInt(requestId));
        Bson update = Updates.set("status", newStatus);
        
        return getRequestCollection().updateOne(filter, update).getModifiedCount() > 0;
    }

    // --- YOUR EXISTING METHODS ---

    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        for (Document doc : getItemCollection().find().limit(50)) {
            itemList.add(mapDocumentToItem(doc));
        }
        return itemList;
    }

    public Item getItemById(String id) {
        Document doc = getItemCollection().find(Filters.eq("item_id", id)).first();
        return (doc != null) ? mapDocumentToItem(doc) : null;
    }

    public void addItem(Item item) {
        Document doc = new Document("item_id", item.getItem_id())
                .append("name", item.getName())
                .append("category", item.getCategory())
                .append("daily_rate", item.getDaily_rate())
                .append("available", item.isAvailable());
        getItemCollection().insertOne(doc);
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

    public void updateItem(String id, Item item) {
        Document updateData = new Document("name", item.getName())
                .append("category", item.getCategory())
                .append("daily_rate", item.getDaily_rate());
        getItemCollection().updateOne(Filters.eq("item_id", id), new Document("$set", updateData));
    }

    public Item removeItem(String id) {
        Item item = getItemById(id);
        if (item != null) getItemCollection().deleteOne(Filters.eq("item_id", id));
        return item;
    }
}