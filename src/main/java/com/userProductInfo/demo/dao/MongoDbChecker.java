package com.userProductInfo.demo.dao;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoDbChecker {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Value("${spring.data.mongodb.uri:NOT_FOUND}")
    private String mongoUri;

    @PostConstruct
    public void printDb() {
    	System.out.println("Mongo URI = " + mongoUri);
        System.out.println("Connected DB = " + mongoTemplate.getDb().getName());
        System.out.println(
                "Factory DB = " +
                mongoTemplate.getMongoDatabaseFactory()
                             .getMongoDatabase()
                             .getName()
            );
    }
}
