package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class SendSMSException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 36588818517211668L;

	public SendSMSException() {
		super();
	}

	public SendSMSException(String msg, Throwable t) {
		super(msg, t);
	}

	public SendSMSException(String msg) {
		super(msg);
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.SEND_SMS_ERROR;
	}
}