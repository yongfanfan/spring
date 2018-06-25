package com.dark.api.third.liumi.bean;

public class OrderRequest extends BaseRequest {

	private String apiver;
	private String des;
	private String token;

	public String getApiver() {
		return apiver;
	}

	public void setApiver(String apiver) {
		this.apiver = apiver;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
