package com.ipms.policy.dto;

import java.util.Date;
import java.util.Set;

import com.ipms.policy.entity.Role;

public class UserResponseDto {
   
	private Long id;
    private String username;
    private Set<Role> roles;
    private Date createdAt;
    private Date updatedAt;

    public UserResponseDto(Long id, String username, Set<Role> roles, Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

	public UserResponseDto() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
