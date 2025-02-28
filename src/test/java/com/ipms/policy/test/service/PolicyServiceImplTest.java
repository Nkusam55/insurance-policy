package com.ipms.policy.test.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.ipms.policy.entity.Policy;
import com.ipms.policy.entity.User;
import com.ipms.policy.exception.ResourceNotFoundException;
import com.ipms.policy.repository.PolicyRepository;
import com.ipms.policy.repository.UserRepository;
import com.ipms.policy.service.impl.PolicyServiceImpl;

@ExtendWith(MockitoExtension.class)
class PolicyServiceImplTest {

    @Mock
    private PolicyRepository policyRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private SecurityContext securityContext;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private PolicyServiceImpl policyService;

    private User mockUser;
    private Policy mockPolicy;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        mockPolicy = new Policy();
        mockPolicy.setId(1L);
        mockPolicy.setName("Health Insurance");
        mockPolicy.setType("Health");
        mockPolicy.setPremiumAmount(500.0);
        mockPolicy.setCreatedBy(1L);

        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(userDetails);
        lenient().when(userDetails.getUsername()).thenReturn("testUser");

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testSavePolicy_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        when(policyRepository.save(any(Policy.class))).thenReturn(mockPolicy);

        Policy policyToSave = new Policy();
        policyToSave.setName("Health Insurance");
        policyToSave.setType("Health");
        policyToSave.setPremiumAmount(500.0);

        Policy savedPolicy = policyService.save(policyToSave);

        assertNotNull(savedPolicy);
        assertEquals("Health Insurance", savedPolicy.getName());
        verify(policyRepository, times(1)).save(any(Policy.class));
    }

    @Test
    void testFindById_Success() {
        when(policyRepository.findById(anyLong())).thenReturn(Optional.of(mockPolicy));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        Policy foundPolicy = policyService.findById(1L);

        assertNotNull(foundPolicy);
        assertEquals(1L, foundPolicy.getId());
        verify(policyRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_PolicyNotFound() {
        when(policyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> policyService.findById(1L));
    }

    @Test
    void testFindAllPolicies() {
        when(policyRepository.findAll()).thenReturn(Arrays.asList(mockPolicy));

        List<Policy> policies = policyService.findAll();

        assertFalse(policies.isEmpty());
        assertEquals(1, policies.size());
        verify(policyRepository, times(1)).findAll();
    }

    @Test
    void testDeletePolicy_Success() {
        when(policyRepository.findById(anyLong())).thenReturn(Optional.of(mockPolicy));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        policyService.delete(1L);

        verify(policyRepository, times(1)).delete(mockPolicy);
    }

    @Test
    void testDeletePolicy_PolicyNotFound() {
        when(policyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> policyService.delete(1L));
    }
}
