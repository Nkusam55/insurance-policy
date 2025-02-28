package com.ipms.policy.test.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ipms.policy.dto.RegisterRequest;
import com.ipms.policy.dto.UserResponseDto;
import com.ipms.policy.entity.Role;
import com.ipms.policy.entity.User;
import com.ipms.policy.exception.ResourceNotFoundException;
import com.ipms.policy.exception.UserAlreadyExistsException;
import com.ipms.policy.repository.UserRepository;
import com.ipms.policy.security.JwtTokenProvider;
import com.ipms.policy.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Set<Role> roles;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");

        roles = new HashSet<>();
        Role role = new Role();
        role.setName("ROLE_USER");
        roles.add(role);

        lenient().when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        lenient().when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });
    }

    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newUser");
        request.setPassword("password123");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto registeredUser = userService.registerUser(request, roles);

        assertNotNull(registeredUser);
        assertEquals("newUser", registeredUser.getUsername());

        verify(userRepository, times(1)).existsByUsername("newUser");
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void testRegisterUser_Failure_UsernameExists() {
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testUser");
        request.setPassword("password123");

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request, roles));

        verify(userRepository, times(0)).save(any(User.class));
    }


    @Test
    void testAuthenticateUser_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);

        when(userRepository.findByUsername("testUser"))
            .thenReturn(Optional.of(user));

        when(jwtTokenProvider.generateToken(user))
            .thenReturn("mockedJwtToken");

        String token = userService.authenticateUser(user);

        assertNotNull(token);
        assertEquals("mockedJwtToken", token);
    }


    @Test
    void testAuthenticateUser_Failure_InvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.authenticateUser(user));

        assertTrue(exception.getMessage().contains("Authentication failed! Invalid username or password."));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof BadCredentialsException);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAuthenticateUser_Failure_UserNotFound() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.authenticateUser(user));

        assertEquals("User not found with username: testUser", exception.getMessage());
    }

}
