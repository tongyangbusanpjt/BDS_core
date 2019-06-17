package com.bnf.bds.type.mapper;

public class ObjectMappingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObjectMappingException() {
		super();
	}

	public ObjectMappingException(String message) {
		super(message);
	}

	public ObjectMappingException(Throwable cause) {
		super(cause);
	}
	
	public ObjectMappingException(String message, Throwable cause) {
		super(message, cause);
	}
}
