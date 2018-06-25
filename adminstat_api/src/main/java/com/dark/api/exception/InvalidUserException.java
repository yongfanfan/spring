package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class InvalidUserException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 36588818517211668L;

	public InvalidUserException() {
		super();
	}

	public InvalidUserException(String msg, Throwable t) {
		super(msg, t);
	}

	public InvalidUserException(String msg) {
		super(msg);
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.INVALID_USER;
	}
}