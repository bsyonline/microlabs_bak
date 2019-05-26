/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.dao;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.rolex.microlabs.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.inc;

/**
 * @author rolex
 * @since 2019
 */
@Component
@Slf4j
public class AccountDao {

    @Value("${mongodb.database}")
    String database;

    String collection="account";
    @Autowired
    MongoClient mongoClient;

    public void transfer(int from, int to, long amount) {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        ClientSession clientSession = mongoClient.startSession();
        try {
            col.updateOne(eq("id", from), inc("amount", amount));
            col.updateOne(and(eq("id", to), gte("amount", amount)), inc("amount", -amount));
            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
        }
    }

    public void insert(Integer id, long amount) {
        MongoCollection<Document> col = mongoClient.getDatabase(database).getCollection(collection);
        Account account = new Account();
        account.setAmount(amount);
        account.setId(id);
        col.insertOne(Document.parse(new Gson().toJson(account)));
        log.info("add {} to account {}, balance is {}", amount, id, amount);
    }

}
