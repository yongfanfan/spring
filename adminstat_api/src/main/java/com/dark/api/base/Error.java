package com.dark.api.base;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Error {

	private static final Logger LOG = LoggerFactory.getLogger(Error.class);

	private int code;

	private String name;

	private String message;

	private String stack;

	public Error() {

	}

	public Error(String message, int code) {
		this.code = code;
		this.message = message;

		LOG.error(message);
	}

	public Error(String message, ErrorCode code) {
		this(message, code.code());
	}

	public Error(Throwable t, int code, boolean debug) {
		while (t.getCause() != null) {
			t = t.getCause();
		}

		this.code = code;
		if(code==ErrorCode.INVALID_INPUT.code()) {
			this.message = t.getMessage();//返回校验信息
		} else {
			this.message = ErrorCode.valueOf(code).message();
		}
		this.name = t.getClass().getName();

		if (debug) {
			StringWriter s = new StringWriter();
			PrintWriter w = new PrintWriter(s);
			t.printStackTrace(w);
			w.flush();
			this.stack = s.toString();
		}
		if (code != ErrorCode.INVALID_INPUT.code()) {
			LOG.error(t.getMessage(), t);//输入校验错误不打印日志
		}
	}

	public Error(Throwable t, ErrorCode code, boolean debug) {
		this(t, code.code(), debug);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}
}
