package com.concert.booking;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MongoConnectionTest {

    @Test
    public void testMongoLocalConnection() {
        String uri = "mongodb://localhost:27017/concert_booking";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("concert_booking");
            Document ping = database.runCommand(new Document("ping", 1));
            System.out.println("====================================================");
            System.out.println(">>> KET NOI THANH CONG MONGODB DOCKER (LOCALHOST)! <<<");
            System.out.println(">>> Ping Response: " + ping.toJson());
            System.out.println("====================================================");
            assertNotNull(ping);
            assertEquals(1.0, ping.getDouble("ok"));
        }
    }
}