package com.ipms.policy.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ipms.policy.entity.Role;
import com.ipms.policy.exception.ResourceNotFoundException;
import com.ipms.policy.repository.RoleRepository;
import com.ipms.policy.service.impl.RolesServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RolesServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RolesServiceImpl rolesService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
    }

    @Test
    void testFindByName() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        Optional<Role> foundRole = rolesService.findByName("ROLE_USER");

        assertTrue(foundRole.isPresent());
        assertEquals("ROLE_USER", foundRole.get().getName());
    }

    @Test
    void testGetRolesByNames() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        Set<Role> roles = rolesService.getRolesByNames(List.of("ROLE_USER"));

        assertNotNull(roles);
        assertEquals(1, roles.size());
    }

    @Test
    void testFindByName_NotFound() {
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> rolesService.getRolesByNames(List.of("ROLE_ADMIN")));
    }
}
