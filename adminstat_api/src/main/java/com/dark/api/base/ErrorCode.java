package com.dark.api.base;

public enum ErrorCode {

	INVALID_REQUEST(1000, "无效的请求参数"),

	INVALID_USER(1001, "用户名或密码错误"),

	INVALID_TOKEN(1002, "无效的访问令牌"),

	USER_DENIED(1003, "无权访问"),

	SERVER_ERROR(1004, "服务内部错误"),

	INVALID_PAGE(1005, "无效的页面"),

	INVALID_METHOD(1006, "不支持的请求方式"),
	
	INVALID_THIRD_INVOKE(1007, "调用第三方接口错误"),
	
	INVALID_INPUT(1008, "数据校验错误"),
	
	PURCHASE_TICKET_ERROR(1009, "对不起，获赠彩票失败"),
	
	SEND_SMS_ERROR(1010,"获取短信验证码失败，请稍后重试"),
	
	UPLOAD_ERROR(1011,"上传文件失败，请稍后重试");

	private int code;

	private String message;

	ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int code() {
		return code;
	}

	public String message() {
		return this.message;
	}

	public static ErrorCode valueOf(int errorCode) {
		for (ErrorCode code : values()) {
			if (code.code == errorCode) {
				return code;
			}
		}
		throw new IllegalArgumentException("No matching constant for ["
				+ errorCode + "]");
	}
}
