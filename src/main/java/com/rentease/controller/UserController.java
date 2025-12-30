package com.rentease.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentease.dto.AuthResponse;
import com.rentease.entity.User;
import com.rentease.service.UserService;
import com.rentease.utils.JWT;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

	
	 @Autowired
	    private UserService userService;
	 
	 @Autowired
	    private JWT jwtUtil;

	    @PostMapping("/register")
	    public User register(@RequestBody User user) {
	        return userService.registerUser(user);
	    }

	    @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody User request) {

	        Optional<User> user = userService.loginUser(request.getEmail(), request.getPassword());

	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body("Invalid email or password");
	        }
	        
	        User u = user.get();

	        String token = jwtUtil.generateToken(u.getEmail(), u.getId());
	        
	        AuthResponse authResponse = new AuthResponse();
	        authResponse.setUserId(u.getId());
	        authResponse.setEmail(u.getEmail());
	        authResponse.setFullName(u.getFullName());
	        authResponse.setPhone(u.getPhone());
	        authResponse.setRole(u.getRole());
	        authResponse.setToken(token);
	        
	        // Return cookie 
//	        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
//	                .httpOnly(true)   // prevent JS hacking
//	                .path("/")
//	                .maxAge(2 * 60 * 60)  // 2 hours
//	                .secure(false)  // use true in production
//	                .build();
	        
	        // return ResponseEntity.ok(authResponse);
	        return ResponseEntity.ok()
			        // .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
			        .body(authResponse);
	    }

	    @GetMapping("/{id}")
	    public Optional<User> getUser(@PathVariable Long id) {
	        return userService.getUserById(id);
	    }

}
