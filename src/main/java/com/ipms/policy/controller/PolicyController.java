package com.ipms.policy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipms.policy.entity.Policy;
import com.ipms.policy.exception.SuccessResponse;
import com.ipms.policy.service.PolicyService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/policies")
@Tag(name = "Policies")
public class PolicyController {

	@Autowired
	private PolicyService policyService;

	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<Policy>> getAllPolicies() {
		List<Policy> policies = policyService.findAll();
		return ResponseEntity.ok(policies);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<Policy> getPolicyById(@PathVariable Long id) {
		Policy policy = policyService.findById(id);
		return ResponseEntity.ok(policy);
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<SuccessResponse> createPolicy(@RequestBody Policy policy) {
		Policy savedPolicy = policyService.save(policy);
		SuccessResponse successResponse = new SuccessResponse("Policy created successfully", savedPolicy);
		return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<SuccessResponse> updatePolicy(@PathVariable Long id, @RequestBody Policy policy) {
		policy.setId(id); 
		Policy updatedPolicy = policyService.save(policy);
		SuccessResponse successResponse = new SuccessResponse("Policy updated successfully", updatedPolicy);
		return ResponseEntity.ok(successResponse);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<SuccessResponse> deletePolicy(@PathVariable Long id) {
		policyService.delete(id);
		SuccessResponse successResponse = new SuccessResponse("Policy with ID " + id + " deleted successfully", null);
		return ResponseEntity.status(HttpStatus.OK).body(successResponse);
	}

}
