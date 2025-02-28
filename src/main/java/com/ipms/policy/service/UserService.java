package com.ipms.policy.service;

import java.util.List;
import java.util.Set;

import com.ipms.policy.dto.RegisterRequest;
import com.ipms.policy.dto.UserResponseDto;
import com.ipms.policy.entity.Role;
import com.ipms.policy.entity.User;

public interface UserService {

	UserResponseDto registerUser(RegisterRequest registerRequest, Set<Role> roles);

	String authenticateUser(User user);

	List<UserResponseDto> findAll();

}
