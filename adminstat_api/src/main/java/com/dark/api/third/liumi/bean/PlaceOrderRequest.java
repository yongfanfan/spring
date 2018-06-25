package com.dark.api.third.liumi.bean;

public class PlaceOrderRequest extends OrderRequest {

	// private Logger logger = Logger.getLogger(getClass());

	private String appver;
	private String extno;
	private String fixtime;
	private String mobile;
	private String postpackage;
	private int ordertype;

	public String getExtno() {
		return extno;
	}

	public void setExtno(String extno) {
		this.extno = extno;
	}

	public String getFixtime() {
		return fixtime;
	}

	public void setFixtime(String fixtime) {
		this.fixtime = fixtime;
	}

	public String getAppver() {
		return appver;
	}

	public void setAppver(String appver) {
		this.appver = appver;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPostpackage() {
		return postpackage;
	}

	public void setPostpackage(String postpackage) {
		this.postpackage = postpackage;
	}

	public int getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(int ordertype) {
		this.ordertype = ordertype;
	}

	public String toSign() {
		// 数组按key排序
		// 拼接字符串
		StringBuffer sign = new StringBuffer();
		if (isNotBlank(getApiver())) {
			sign.append("apiver" + getApiver());
		}
		sign.append("appkey" + getAppkey());
		if (isNotBlank(getAppsecret())) {
			sign.append("appsecret" + getAppsecret());
		}
		sign.append("appver" + getAppver());
		if (isNotBlank(getDes())) {
			sign.append("des" + getDes());
		}
		if (isNotBlank(getExtno())) {
			sign.append("extno" + getExtno());
		}
		if (isNotBlank(getFixtime())) {
			sign.append("fixtime" + getFixtime());
		}
		sign.append("mobile" + getMobile());
		if (getOrdertype() > 0) {
			sign.append("ordertype" + getOrdertype());
		}
		sign.append("postpackage" + getPostpackage());
		sign.append("token" + getToken());
		return sign.toString();
	}

	private boolean isNotBlank(String str)
	{
		if (str == null)
		{
			return false;
		}
		int len = str.length();
		for (int i = 0; i < len; i++)
		{
			if (!Character.isWhitespace(str.codePointAt(i)))
			{
				// found a non-whitespace, we can stop searching now
				return true;
			}
		}
		// only whitespace
		return false;
	}
}
