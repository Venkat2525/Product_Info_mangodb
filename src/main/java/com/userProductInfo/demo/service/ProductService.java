package com.userProductInfo.demo.service;

import com.userProductInfo.demo.dao.Order;
import com.userProductInfo.demo.dao.Product;
import com.userProductInfo.demo.dao.User;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

	@Autowired
    private  ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
    private  UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getById(String id) {   
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
    @Transactional(rollbackFor = Exception.class)
	public Product save(Product product) throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		auth.getName();
		
		User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() ->
                    new UsernameNotFoundException("User not found: " + auth.getName()));
		Order order = orderService.getOrder();
		order.setUser(user);
		order.setProduct(product);
		orderRepository.save(order);
		
		  if (true) {
		        throw new RuntimeException("Testing Transaction Rollback");
		    }
		
		  System.out.println("Reaching here");
		return productRepository.save(product);
	}

    @Transactional
    public Product update(String id, Product updatedProduct) {
        Product existing = getById(id);
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        return productRepository.save(existing);
    }
    
    @Transactional
    public void delete(String id)
    {
    	productRepository.deleteById(id);
    }
}