/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.dao;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.rolex.microlabs.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonInt32;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.*;

/**
 * @author rolex
 * @since 2019
 */
@Component
@Slf4j
public class OrderDao {

    @Value("${mongodb.database}")
    String database;
    String collection = "order";
    @Autowired
    MongoClient mongoClient;

    public void remove() {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        col.deleteMany(new BasicDBObject());
    }

    public void save(Order order) {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        String json = new Gson().toJson(order);
        Document document = new Document().parse(json);
        col.insertOne(document);
    }

    public void save(List<Order> order) {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        col.insertMany(order.stream().map(o -> new Document().parse(new Gson().toJson(o))).collect(Collectors.toList()));
    }

    public long count() {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        long count = col.countDocuments();
        return count;
    }

    public void sortByUserDesc() {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        BasicDBObject sortObject = new BasicDBObject("userId", -1);
        sortObject.put("_id", 1);
        MongoCursor<Document> cursor = col.find()
                .sort(sortObject)
                .iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            System.out.println(new Gson().toJson(document));
        }
    }

    public void groupByUser() {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        MongoCursor<Document> cursor = col.aggregate(Lists.newArrayList(
                group("$userId", sum("count", new BsonInt32(1)))
        )).iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            System.out.println(new Gson().toJson(document));
        }
    }

    public void groupByCustomerAndUser() {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        MongoCursor<Document> cursor = col.aggregate(Lists.newArrayList(
                group(new BasicDBObject("customerId", "$customerId").append("userId", "$userId"), sum("count", new BsonInt32(1)))
        )).iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            System.out.println(new Gson().toJson(document));
        }
    }

    public void findByCustomerDistinctByUser(Integer customerId) {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        MongoCursor<Document> cursor = col.aggregate(Lists.newArrayList(
                match(eq("customerId", customerId)),
                group(new BasicDBObject("customerId", "$customerId").append("userId", "$userId"), sum("count", new BsonInt32(1))),
                group("$_id.customerId", sum("count", new BsonInt32(1)))
        )).iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            System.out.println(new Gson().toJson(document));
        }
    }

    public void findByCustomerDistinctByUser1(Integer customerId) {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        String query1 = "{$match:{\"customerId\": 1}}";
        String query2 = "{$group:{_id:{customerId:\"$customerId\",userId:\"$userId\"}}}";
        String query3 = "{$group:{_id:\"$_id.customerId\",count:{\"$sum\":1}}}";
        Bson bson1 = BasicDBObject.parse(query1);
        Bson bson2 = BasicDBObject.parse(query2);
        Bson bson3 = BasicDBObject.parse(query3);
        MongoCursor<Document> cursor = col.aggregate(Lists.newArrayList(bson1, bson2, bson3)).iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            System.out.println(new Gson().toJson(document));
        }
    }

    public List<Order> findAll(int page, int pageSize) {
        List<Order> orders = Lists.newArrayList();
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        MongoCursor<Document> cursor = col.find()
                .skip(page * pageSize)
                .limit(pageSize)
                .iterator();
        Gson gson = new Gson();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String objectId = document.get("_id").toString();
            String json = gson.toJson(document);
            Order order = gson.fromJson(json, Order.class);
            order.setObjectId(objectId);
            orders.add(order);
        }
        return orders;
    }

    public List<Order> findAll(String head, int batchSize, int limit) {
        List<Order> orders = Lists.newArrayList();
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        MongoCursor<Document> cursor = col.find(and(gt("_id", new ObjectId(head))))
                .batchSize(batchSize).limit(limit).iterator();
        Gson gson = new Gson();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String objectId = document.get("_id").toString();
            Order order = gson.fromJson(document.toJson(), Order.class);
            order.setObjectId(objectId);
            orders.add(order);
        }
        return orders;
    }
}
