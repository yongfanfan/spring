package com.dark.api.third.liumi.bean;

import com.dark.common.util.JsonUtil;

public class CallbackResponse {
	private String orderNo;
	private String extNo;
	private String status;
	private String code;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getExtNo() {
		return extNo;
	}
	public void setExtNo(String extNo) {
		this.extNo = extNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return JsonUtil.writeValueAsString(this);
	}
}
