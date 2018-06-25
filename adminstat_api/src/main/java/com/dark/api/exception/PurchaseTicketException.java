package com.dark.api.exception;

import com.dark.api.base.ErrorCode;

public class PurchaseTicketException extends ClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6440504821460205743L;

	public PurchaseTicketException(String msg, Throwable t) {
		super(msg, t);
	}

	public PurchaseTicketException(String msg) {
		super(msg);
	}

	public PurchaseTicketException() {
	}

	@Override
	public ErrorCode code() {
		return ErrorCode.PURCHASE_TICKET_ERROR;
	}

}
