package com.userProductInfo.demo.Controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.userProductInfo.demo.dao.AuthRequest;
import com.userProductInfo.demo.dao.AuthResponse;
import com.userProductInfo.demo.dao.GoogleLoginRequest;
import com.userProductInfo.demo.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
    private AuthService authService;
     
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest req,  BindingResult result) {
    	
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
        authService.register(req);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest req,  BindingResult result) {
    	
    	 if (result.hasErrors()) {
             Map<String, String> errors = new HashMap<>();
             result.getFieldErrors().forEach(error ->
                 errors.put(error.getField(), error.getDefaultMessage())
             );
             return ResponseEntity.badRequest().body(errors);
         }
    	 
        return ResponseEntity.ok(authService.login(req));
    }
    
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest request) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(authService.googleLogin(request.getIdToken()));
    }
}
