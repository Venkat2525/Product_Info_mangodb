package com.userProductInfo.demo.service;

import com.userProductInfo.demo.dao.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}