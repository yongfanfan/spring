package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class ValidatorException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 36588818517211668L;

	public ValidatorException() {
		super();
	}

	public ValidatorException(String msg, Throwable t) {
		super(msg, t);
	}

	public ValidatorException(String msg) {
		super(msg);
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.INVALID_INPUT;
	}
}