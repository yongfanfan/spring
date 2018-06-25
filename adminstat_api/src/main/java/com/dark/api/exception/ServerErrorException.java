package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class ServerErrorException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 705980760295565056L;

	public ServerErrorException() {

	}

	public ServerErrorException(String msg) {
		super(msg);
	}

	public ServerErrorException(String msg, Throwable t) {
		super(msg, t);
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.SERVER_ERROR;
	}

}