package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class ClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3817573290201955838L;

	public ClientException(String msg, Throwable t) {
		super(msg, t);
	}

	public ClientException(String msg) {
		super(msg);
	}

	public ClientException() {

	}

	public ClientException(Throwable arg0) {
		super(arg0);
	}

	public ErrorCode code() {
		return ErrorCode.INVALID_REQUEST;
	}
}
