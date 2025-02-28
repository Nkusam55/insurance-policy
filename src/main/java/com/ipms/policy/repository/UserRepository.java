package com.ipms.policy.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ipms.policy.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  
	@EntityGraph(attributePaths = "roles")
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

}



