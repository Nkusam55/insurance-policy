package com.ipms.policy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ipms.policy.entity.Policy;
import com.ipms.policy.entity.User;
import com.ipms.policy.exception.ResourceNotFoundException;
import com.ipms.policy.repository.PolicyRepository;
import com.ipms.policy.repository.UserRepository;
import com.ipms.policy.service.PolicyService;

@Service
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	private PolicyRepository policyRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public Policy save(Policy policy) {
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User currentUser = userRepository.findByUsername(userDetails.getUsername())
					.orElseThrow(() -> new RuntimeException("User not found"));

			if (policy.getCreatedBy() == null) {
				policy.setCreatedBy(currentUser.getId());
			}
            if(policy.getId() != null ){
              findById(policy.getId());
			}
			return policyRepository.save(policy);
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while saving the policy: " + e.getMessage());
		}
	}

	@Override
	public Policy findById(Long id) {
		try {
			Policy policy = policyRepository.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException("Policy not found with id: " + id));
			checkOwnership(policy);
			return policy;
		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while fetching the policy: " + e.getMessage());
		}
	}

	@Override
	public List<Policy> findAll() {
		try {
			return policyRepository.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while fetching policies: " + e.getMessage());
		}
	}

	@Override
	public void delete(Long id) {
		try {
			Policy policy = policyRepository.findById(id).orElseThrow(() -> 
			new ResourceNotFoundException("Policy not found with id: " + id));
			checkOwnership(policy);
			policyRepository.delete(policy);
		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while deleting the policy: " + e.getMessage());
		}
	}

	private void checkOwnership(Policy policy) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));
		if (!(policy.getCreatedBy() == currentUser.getId()) && !isAdmin()) {
			throw new RuntimeException("Access denied: You can only modify your own policies.");
		}
	}

	private boolean isAdmin() {
		return SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
	}

}
