package com.dark.api.third.liumi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dark.api.third.liumi.bean.PlaceOrderRequest;
import com.dark.api.third.liumi.bean.TokenRequest;

public class ApiClient {

	// 要测试的参数，请自行填入
	private String host = "http://yfbapi.liumi.com";
	private String appkey = "PXpNNcRmlu";
	private String appsecret = "QKoZ8HaXD4wbL4LC";
	private String mobile = "13880378674";
	private String postpackage = "YD10;DX5;LT50";

	public static void main(String[] args) throws NoSuchAlgorithmException,
			IOException {
		ApiClient apiClient = new ApiClient();
		String token = apiClient.getToken();
		apiClient.placeOrder(token);
	}

	private void placeOrder(String token) throws NoSuchAlgorithmException,
			IOException {
		PlaceOrderRequest orderRequest = new PlaceOrderRequest();
		orderRequest.setAppkey(appkey);
		String appver = "Http";
		orderRequest.setAppver(appver);
		String extno = "unittest";
		orderRequest.setExtno(extno);
		orderRequest.setFixtime("");
		orderRequest.setMobile(mobile);
		orderRequest.setPostpackage(postpackage);
		orderRequest.setApiver("2.0");
		orderRequest.setDes("0");
		orderRequest.setToken(getToken());
		String sign = SHA1.getSHA1(orderRequest.toSign());
		byte[] data = ("{\"appkey\":\"" + appkey + "\","
				+ "\"appver\":\"" + appver + "\","
				+ "\"apiver\":\"2.0\",\"des\":\"0\","
				+ "\"extno\":\"" + extno + "\","
				+ "\"fixtime\":\"\","
				+ "\"mobile\":\"" + mobile + "\","
				+ "\"postpackage\":\"" + postpackage + "\","
				+ "\"token\":\"" + token + "\","
				+ "\"sign\":\"" + sign + "\"}").getBytes();
		String result = httpPost("/server/placeOrder", data);
		System.out.println("result:" + result);
	}

	private String getToken() throws IOException, NoSuchAlgorithmException {
		TokenRequest tokenRequest = new TokenRequest();
		tokenRequest.setAppkey(appkey);
		String md5AppSecret = MD5Util.string2MD5(appsecret);
		tokenRequest.setAppsecret(md5AppSecret);
		String sign = SHA1.getSHA1(tokenRequest.toSign());
		byte[] data = ("{\"appkey\":\"" + appkey
				+ "\",\"appsecret\":\"" + md5AppSecret
				+ "\",\"sign\":\"" + sign + "\"}")
				.getBytes();

		String result = httpPost("/server/getToken", data);
		System.out.println("getTokenResult:" + result);
		Pattern p = Pattern.compile("\"token\":\"(.*)\"");
		Matcher m = p.matcher(result);
		m.find();
		String token = m.group(1);
		System.out.println("token:" + token);
		return token;
	}

	private String httpPost(String url, byte[] data)
			throws IOException {
		URL httpUrl = new URL(host + url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl
				.openConnection();
		httpURLConnection.setConnectTimeout(3000);
		httpURLConnection.setDoInput(true);// 从服务器获取数据
		httpURLConnection.setDoOutput(true);// 向服务器写入数据
		// 设置请求体的类型
		httpURLConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		httpURLConnection.setRequestProperty("Content-Lenth",
				String.valueOf(data.length));
		// 获得输出流，向服务器输出数据
		OutputStream outputStream = (OutputStream) httpURLConnection
				.getOutputStream();
		outputStream.write(data);

		String result = null;
		// 获得服务器响应的结果和状态码
		int responseCode = httpURLConnection.getResponseCode();
		InputStream inputStream;
		if (responseCode == 200) {
			// 获得输入流，从服务器端获得数据
			inputStream = (InputStream) httpURLConnection.getInputStream();
			// } else {
			// inputStream = (InputStream) httpURLConnection.getErrorStream();
			// }
			// 把从输入流InputStream按指定编码格式encode变成字符串String
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			if (inputStream != null) {
				while ((len = inputStream.read(buffer)) != -1) {
					byteArrayOutputStream.write(buffer, 0, len);
				}
				result = new String(byteArrayOutputStream.toByteArray(),
						"UTF-8");
			}
		}
		return result;
	}
}
