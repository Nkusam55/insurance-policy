package com.ipms.policy.exception;

public class ResourceNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8796550522169411247L;

	public ResourceNotFoundException() {}

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}