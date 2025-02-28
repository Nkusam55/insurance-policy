package com.ipms.policy.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ipms.policy.dto.RegisterRequest;
import com.ipms.policy.dto.UserResponseDto;
import com.ipms.policy.entity.Role;
import com.ipms.policy.entity.User;
import com.ipms.policy.exception.ResourceNotFoundException;
import com.ipms.policy.exception.UserAlreadyExistsException;
import com.ipms.policy.repository.UserRepository;
import com.ipms.policy.security.JwtTokenProvider;
import com.ipms.policy.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public UserResponseDto registerUser(RegisterRequest registerRequest, Set<Role> roles) {
		try {
			if (userRepository.existsByUsername(registerRequest.getUsername())) {
				throw new UserAlreadyExistsException("Username already exists.");
			}

			User user = new User();
			user.setUsername(registerRequest.getUsername());
			user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
			user.setRoles(roles);
			userRepository.save(user);
			UserResponseDto response = new UserResponseDto(user.getId(), user.getUsername(), user.getRoles(), user.getCreatedAt(), user.getUpdatedAt());
			return response;
		} catch (UserAlreadyExistsException e) {
			throw new UserAlreadyExistsException("User with this username already exists");
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while registering the user. Please try again later.", e);
		}
	}

	@Override
	public String authenticateUser(User user) {
		try {
			Authentication authentication = 
					authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

			if (authentication.isAuthenticated()) {
				Optional<User> originalUser = userRepository.findByUsername(user.getUsername());

				if (originalUser.isEmpty()) {
					throw new ResourceNotFoundException("User not found with username: " + user.getUsername());
				}
				return jwtTokenProvider.generateToken(originalUser.get());
			} else {
				return "Invalid Credentials!";
			}
		} catch (AuthenticationException e) {
			throw new RuntimeException("Authentication failed! Invalid username or password.", e);
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException("User not found with username: " + user.getUsername());
		} catch (Exception e) {
			throw new RuntimeException("An error occurred during authentication. Please try again later.", e);
		}
	}

	@Override
	public List<UserResponseDto> findAll() {
		try {
			List<User> users = userRepository.findAll();
			return users.stream().map(user -> new UserResponseDto(
					user.getId(),
					user.getUsername(),
					user.getRoles(),
					user.getCreatedAt(),
					user.getUpdatedAt()
					)).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while fetching users", e);
		}
	}

}
