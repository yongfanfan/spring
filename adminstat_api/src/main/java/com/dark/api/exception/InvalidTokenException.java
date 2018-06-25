package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class InvalidTokenException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7587761084904345935L;

	public InvalidTokenException(String msg, Throwable t) {
		super(msg, t);
	}

	public InvalidTokenException(String msg) {
		super(msg);
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.INVALID_TOKEN;
	}
}
