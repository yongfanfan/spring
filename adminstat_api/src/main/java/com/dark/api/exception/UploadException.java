package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class UploadException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6440504821460205743L;

	public UploadException(String msg, Throwable t) {
		super(msg, t);
	}

	public UploadException(String msg) {
		super(msg);
	}

	public UploadException() {
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.UPLOAD_ERROR;
	}

}
