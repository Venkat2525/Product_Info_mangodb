package com.userProductInfo.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.userProductInfo.demo.dao.Order;

@Component
public class OrderService {
	
	@Autowired
	private AddressService address;
	
	
	public Order getOrder()
	{
		Order order = new Order();
		order.setOrderId(UUID.randomUUID().toString());
		order.setAddress(address.setAddress());
		order.setUser(null);
		
		return order;
	}
}
