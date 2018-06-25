package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class UserDeniedAuthorizationException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6567840352806374013L;

	public UserDeniedAuthorizationException(String msg, Throwable t) {
		super(msg, t);
	}

	public UserDeniedAuthorizationException(String msg) {
		super(msg);
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.USER_DENIED;
	}

}
