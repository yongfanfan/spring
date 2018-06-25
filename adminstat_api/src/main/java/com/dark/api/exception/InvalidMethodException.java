package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class InvalidMethodException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6440504821460205743L;

	public InvalidMethodException(String msg, Throwable t) {
		super(msg, t);
	}

	public InvalidMethodException(String msg) {
		super(msg);
	}

	public InvalidMethodException() {
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.INVALID_METHOD;
	}

}
