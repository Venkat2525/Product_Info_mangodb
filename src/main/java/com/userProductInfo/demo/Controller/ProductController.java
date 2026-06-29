package com.userProductInfo.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userProductInfo.demo.dao.Product;
import com.userProductInfo.demo.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	@Autowired
    private ProductService productService;

    // Both USER and ADMIN can read
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ROLE_ADMIN')")
    public ResponseEntity<Product> getById(@PathVariable String id) {  
        return ResponseEntity.ok(productService.getById(id));
    }

    // Only ADMIN can create/update
	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Product> create(@RequestBody Product product) throws Exception {
		return ResponseEntity.status(201).body(productService.save(product));
	}

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> update(@PathVariable String id,    
                                          @RequestBody Product product) {
        return ResponseEntity.ok(productService.update(id, product));
    }
        
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> delete(String id)
    {
    	productService.delete(id);
       return ResponseEntity.ok("Product with id + id + got deleted");
    }
}