package com.ipms.policy.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipms.policy.dto.LoginRequest;
import com.ipms.policy.dto.RegisterRequest;
import com.ipms.policy.dto.UserResponseDto;
import com.ipms.policy.entity.Role;
import com.ipms.policy.entity.User;
import com.ipms.policy.exception.SuccessResponse;
import com.ipms.policy.service.RolesService;
import com.ipms.policy.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RolesService rolesService;
	
	@PostMapping("/register")
	public ResponseEntity<SuccessResponse> registerUser(@RequestBody RegisterRequest request) {

		Set<Role> roles = new HashSet<>();
		for (String roleName : request.getRoles()) {
			Role role = rolesService.findByName(roleName).orElseThrow(() -> 
			new RuntimeException("Role not found"));
			roles.add(role);
		}

		UserResponseDto user = userService.registerUser(request, roles);
		SuccessResponse successResponse = new SuccessResponse("User registered successfully", user);
		return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<SuccessResponse> login(@RequestBody LoginRequest loginRequest) {
		User user = new User();
		user.setUsername(loginRequest.getUsername());
		user.setPassword(loginRequest.getPassword());
		String token = userService.authenticateUser(user);
		SuccessResponse successResponse = new SuccessResponse("Token generated successfully", token);
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
	
	@GetMapping("/users")
	public ResponseEntity<SuccessResponse> getAllUsers() {
		List<UserResponseDto> list = userService.findAll();
		SuccessResponse successResponse = new SuccessResponse("All users fetched successfully", list);
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}
}


