package com.exam;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DB_NAME = "secure_exam";

    private static MongoClient mongoClient;

    public static synchronized MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }
        return mongoClient.getDatabase(DB_NAME);
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
