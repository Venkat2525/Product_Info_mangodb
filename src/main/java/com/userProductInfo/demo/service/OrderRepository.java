package com.userProductInfo.demo.service;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.userProductInfo.demo.dao.Order;

public interface OrderRepository extends MongoRepository<Order, String>{

}
