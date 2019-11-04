package com.rolex.microlabs;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static com.mongodb.client.model.Filters.*;

public class CursorTest {

    @Value("${mongodb.database}")
    String database;
    String collection = "sub";
    @Autowired
    MongoClient mongoClient;


    @Before
    public void add (){
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);

    }

    public void cursor(String[] args) {
        try {
            MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);

            BasicDBObject basicDBObject = new BasicDBObject();
            String start = "2019-08-08 00:00:00";
            String end = "2019-08-09 00:00:00";
            Bson bson = and(gte("idt", start), lt("idt", end));
            FindIterable<Document> findIterable = col.find(bson);
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            int i = 0;
            while (mongoCursor.hasNext()) {
                System.out.println(mongoCursor.next());
                i++;
            }
            System.out.println("i=" + i);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

}
