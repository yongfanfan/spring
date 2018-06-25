package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class ThirdInvokeException extends ClientException {


	private static final long serialVersionUID = -5401041331410846275L;

	public ThirdInvokeException(String msg, Throwable t) {
		super(msg, t);
	}

	public ThirdInvokeException(String msg) {
		super(msg);
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.INVALID_THIRD_INVOKE;
	}
}
