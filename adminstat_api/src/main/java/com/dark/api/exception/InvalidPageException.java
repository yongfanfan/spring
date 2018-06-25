package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class InvalidPageException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6440504821460205743L;

	public InvalidPageException(String msg, Throwable t) {
		super(msg, t);
	}

	public InvalidPageException(String msg) {
		super(msg);
	}

	public InvalidPageException() {
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.INVALID_PAGE;
	}

}
