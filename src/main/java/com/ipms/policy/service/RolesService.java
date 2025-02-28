package com.ipms.policy.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ipms.policy.entity.Role;

public interface RolesService {

	Set<Role> getRolesByNames(List<String> roles);

	Role save(Role role);

	Optional<Role> findByName(String roleName);

}