package cyclenest.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import cyclenest.model.Item; // Replace with your actual Model class name

public class MongoSeeder {

    // 1. Paste your MongoDB Connection String here
    private static final String CONNECTION_STRING = "mongodb+srv://n1085361:CycleNest123@cyclenestcluster.9ibdwx0.mongodb.net/?appName=CycleNestCluster";
    private static final String DATABASE_NAME = "CycleNestDB";
    private static final String COLLECTION_NAME = "items";

    public void seedData() {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Clear existing data so you don't have duplicates
            collection.drop();

            // 2. Load the file from the Maven resources folder
            InputStream is = getClass().getClassLoader().getResourceAsStream("data/items.json");
            
            if (is == null) {
                System.out.println("Error: JSON file not found in src/main/resources/data/");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = new JsonFactory();
            List<Document> batch = new ArrayList<>();

            try (JsonParser parser = factory.createParser(is)) {
                // Check if the first token is the start of an array '['
                if (parser.nextToken() != JsonToken.START_ARRAY) {
                    throw new IllegalStateException("Expected an array of items");
                }

                while (parser.nextToken() == JsonToken.START_OBJECT) {
                    // Read one JSON object and convert to a Map/Document
                    Document doc = Document.parse(mapper.readTree(parser).toString());
                    batch.add(doc);

                    // Insert in batches of 1000 for speed
                    if (batch.size() >= 1000) {
                        collection.insertMany(batch);
                        batch.clear();
                        System.out.println("Uploaded 1000 items...");
                    }
                }
                
                // Insert remaining items
                if (!batch.isEmpty()) {
                    collection.insertMany(batch);
                }
            }

            System.out.println("Successfully seeded 10,000 items to MongoDB!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        MongoSeeder seeder = new MongoSeeder();
        seeder.seedData();
    }
}