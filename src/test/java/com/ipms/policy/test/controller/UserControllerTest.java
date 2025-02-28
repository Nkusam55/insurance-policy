package com.ipms.policy.test.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipms.policy.controller.UserController;
import com.ipms.policy.dto.LoginRequest;
import com.ipms.policy.dto.RegisterRequest;
import com.ipms.policy.dto.UserResponseDto;
import com.ipms.policy.entity.Role;
import com.ipms.policy.entity.User;
import com.ipms.policy.service.RolesService;
import com.ipms.policy.service.UserService;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@Mock
	private RolesService rolesService;  

	@InjectMocks
	private UserController userController;

	private ObjectMapper objectMapper = new ObjectMapper();

	private Set<Role> roles;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		roles = new HashSet<>();
		Role role = new Role();
		role.setName("ROLE_USER");
		roles.add(role);
	}

	@Test
	void testRegisterUser() throws Exception {
		RegisterRequest request = new RegisterRequest();
		request.setUsername("newUser");
		request.setPassword("password123");
		request.setRoles(List.of("ROLE_USER")); 

		Role mockRole = new Role();
		mockRole.setName("ROLE_USER");

		UserResponseDto mockUser = new UserResponseDto();
		mockUser.setUsername("newUser");
		mockUser.setRoles(Set.of(mockRole)); 

		when(rolesService.findByName("ROLE_USER")).thenReturn(Optional.of(mockRole));

		when(userService.registerUser(any(RegisterRequest.class), anySet())).thenReturn(mockUser);

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.message").value("User registered successfully"));
	}

	@Test
	void testLogin() throws Exception {
	    LoginRequest loginRequest = new LoginRequest("testUser", "password123");

	    User mockUser = new User();
	    mockUser.setUsername(loginRequest.getUsername());
	    mockUser.setPassword(loginRequest.getPassword());

	    when(userService.authenticateUser(any(User.class))).thenReturn("mockJwtToken");

	    mockMvc.perform(post("/api/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(loginRequest)))
	            .andDo(print()) 
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.message").value("Token generated successfully"))
	            .andExpect(jsonPath("$.data").value("mockJwtToken")); 
	}




}
