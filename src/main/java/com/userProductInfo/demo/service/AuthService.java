package com.userProductInfo.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.userProductInfo.demo.dao.AuthRequest;
import com.userProductInfo.demo.dao.AuthResponse;
import com.userProductInfo.demo.dao.User;
import com.userProductInfo.demo.dao.User.Role;
import com.userProductInfo.demo.jwt.JwtUtil;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Value("${google.oauth.client-id}")
	private String googleClientId;

	public void register(AuthRequest req) {
		User user = new User();
		user.setUsername(req.getUsername());
		user.setPassword(passwordEncoder.encode(req.getPassword()));
		user.setRole(User.Role.valueOf(req.getRole()));
		userRepository.save(user);
	}

	public AuthResponse login(AuthRequest req) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
		User user = userRepository.findByUsername(req.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

		return new AuthResponse(token);
	}
	
	
	public AuthResponse googleLogin(String idTokenString) throws GeneralSecurityException, IOException {
	    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
	            GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory())
	            .setAudience(Collections.singletonList(googleClientId))
	            .build();

	    GoogleIdToken idToken;
	    try {
	        idToken = verifier.verify(idTokenString);
	    } catch (Exception e) {
	        throw new RuntimeException("Invalid Google token", e);
	    }

	    if (idToken == null) {
	        throw new RuntimeException("Invalid or expired Google token");
	    }

	    GoogleIdToken.Payload payload = idToken.getPayload();
	    String email = payload.getEmail();

	    User user = userRepository.findByUsername(email)
	            .orElseGet(() -> {
	                User newUser = new User();
	                newUser.setUsername(email);
	                // Google users never log in with a password, so a random
	                // placeholder satisfies any not-null constraint without being usable
	                newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
	                newUser.setRole(Role.ROLE_ADMIN); // adjust to whatever your enum's default role is called
	                return userRepository.save(newUser);
	            });

	    String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
	    return new AuthResponse(token);
	}
}