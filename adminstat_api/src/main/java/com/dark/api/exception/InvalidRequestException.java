package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class InvalidRequestException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6440504821460205743L;

	public InvalidRequestException(String msg, Throwable t) {
		super(msg, t);
	}

	public InvalidRequestException(String msg) {
		super(msg);
	}

	public InvalidRequestException() {
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.INVALID_REQUEST;
	}

}
