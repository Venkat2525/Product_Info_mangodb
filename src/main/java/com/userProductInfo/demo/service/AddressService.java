package com.userProductInfo.demo.service;

import org.springframework.stereotype.Component;

import com.userProductInfo.demo.dao.Address;

@Component
public class AddressService {
	
	public Address setAddress()
	{
		Address address = new Address();
		address.setCity("Hyderabad");
		address.setPincode("500055");
		address.setState("Telangana");
		return address;
	}
}
