package com.ipms.policy.service;

import java.util.List;

import com.ipms.policy.entity.Policy;

public interface PolicyService {

	List<Policy> findAll();

	Policy findById(Long id);

	Policy save(Policy policy);

	void delete(Long id);
	
}
