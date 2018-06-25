package com.dark.api.third.liumi.bean;

public class CheckRequest extends BaseRequest {

	private String token;
	private String mobile;
	private String packageid;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String packageid) {
		this.packageid = packageid;
	}
	
	public String toSign() {
		// 数组按key排序
		// 拼接字符串
		StringBuffer sign = new StringBuffer();
		
		sign.append("appkey" + getAppkey());
		if (isNotBlank(getAppsecret())) {
			sign.append("appsecret" + getAppsecret());
		}
		sign.append("mobile" + getMobile());
		sign.append("packageid" + getPackageid());
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
