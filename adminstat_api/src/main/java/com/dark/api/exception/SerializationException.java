package com.dark.api.exception;

public class SerializationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7113975575026032781L;

	public SerializationException() {
	}

	public SerializationException(String message) {
		super(message);
	}

	public SerializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SerializationException(Throwable cause) {
		super(cause);
	}
}
