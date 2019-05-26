/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootApplication
public class MongoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongoApplication.class, args);
    }

    @Value("${mongodb.url}")
    String url;

    @Bean
    public MongoClient mongoClient() {
        MongoClientURI connectionString = new MongoClientURI(url);
        MongoClient mongoClient = new MongoClient(connectionString);
        return mongoClient;
    }
}
