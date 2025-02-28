package com.ipms.policy.exception;


public class UserAlreadyExistsException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1710727666119535725L;

	public UserAlreadyExistsException() {}

	public UserAlreadyExistsException(String msg) {
		super(msg);
	}
}

