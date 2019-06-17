package com.bnf.bds;

/**
 * 
 * @author SJH
 */
public class EgovPlusException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EgovPlusException() {
		super();
	}

	public EgovPlusException(String message) {
		super(message);
	}

	public EgovPlusException(Throwable cause) {
		super(cause);
	}
	
	public EgovPlusException(String message, Throwable cause) {
		super(message, cause);
	}
}
