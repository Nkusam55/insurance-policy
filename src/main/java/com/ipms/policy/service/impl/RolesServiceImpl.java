package com.ipms.policy.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipms.policy.entity.Role;
import com.ipms.policy.exception.ResourceNotFoundException;
import com.ipms.policy.repository.RoleRepository;
import com.ipms.policy.service.RolesService;

@Service
public class RolesServiceImpl implements RolesService {

    @Autowired
    private RoleRepository roleRepository;

    public Set<Role> getRolesByNames(List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
            roles.add(role);
        }
        return roles;
    }
    
    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

	@Override
	public Optional<Role> findByName(String roleName) {
		return roleRepository.findByName(roleName);
	}

}
