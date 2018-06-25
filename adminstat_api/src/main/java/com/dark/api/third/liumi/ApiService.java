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

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.dark.api.third.liumi.bean.CheckRequest;
import com.dark.api.third.liumi.bean.CheckResponse;
import com.dark.api.third.liumi.bean.OrderResponse;
import com.dark.api.third.liumi.bean.PlaceOrderRequest;
import com.dark.api.third.liumi.bean.TokenRequest;
import com.dark.common.domain.Constants;

@Component
public class ApiService {
	// 要测试的参数，请自行填入
//		private String host = "http://yfbapi.liumi.com";
//		private String appkey = "PXpNNcRmlu";
//		private String appsecret = "QKoZ8HaXD4wbL4LC";
//		private String host="http://api.tenchang.com";
//		private String appkey = "ydDlnIZFLW";
//		private String appsecret = "u6uz4kA4X8mxbqUB";

		public OrderResponse placeOrder(Long orderId,String postpackage,String mobile) {
			PlaceOrderRequest orderRequest = new PlaceOrderRequest();
			orderRequest.setAppkey(Constants.LIUMI_APPKEY);
			String appver = "Http";
			orderRequest.setAppver(appver);
			String extno = orderId.toString();
			orderRequest.setExtno(extno);
			orderRequest.setFixtime("");
			orderRequest.setMobile(mobile);
			orderRequest.setPostpackage(postpackage);
			orderRequest.setApiver("2.0");
			orderRequest.setDes("0");
			try {
				orderRequest.setToken(getToken());
				String sign = SHA1.getSHA1(orderRequest.toSign());
				byte[] data = ("{\"appkey\":\"" + Constants.LIUMI_APPKEY + "\","
						+ "\"appver\":\"" + appver + "\","
						+ "\"apiver\":\"2.0\",\"des\":\"0\","
						+ "\"extno\":\"" + extno + "\","
						+ "\"fixtime\":\"\","
						+ "\"mobile\":\"" + mobile + "\","
						+ "\"postpackage\":\"" + postpackage + "\","
						+ "\"token\":\"" + getToken() + "\","
						+ "\"sign\":\"" + sign + "\"}").getBytes();
				String result = httpPost("/server/placeOrder", data);
				System.out.println(result);
				JSONObject jsonObject=JSONObject.fromObject(result);
				String code=jsonObject.getString("code");
				OrderResponse response=new OrderResponse();
				response.setCode(code);
				if("000".equals(code)){
					response.setIsSuccess(true);
					response.setFlowno(jsonObject.getJSONObject("data").getString("orderNO"));
				}else if("024".equals(code)){
					response.setIsFinished(true);
				}
				return response;
			} catch (NoSuchAlgorithmException | IOException e) {
				return null;
			}
			
		}
		
		public CheckResponse checkLTPhone(String mobile,String postpackage){
			CheckRequest checkRequest = new CheckRequest();
			checkRequest.setAppkey(Constants.LIUMI_APPKEY);
			checkRequest.setMobile(mobile);
			checkRequest.setPackageid(postpackage);
			String md5AppSecret = MD5Util.string2MD5(Constants.LIUMI_APPSECRET);
			checkRequest.setAppsecret(md5AppSecret);
			try {
				checkRequest.setToken(getToken());
				String sign = SHA1.getSHA1(checkRequest.toSign());
				byte[] data = ("{\"appkey\":\"" + Constants.LIUMI_APPKEY + "\","
						+ "\"appsecret\":\""+md5AppSecret+"\","
						+ "\"mobile\":\"" + mobile + "\","
						+ "\"packageid\":\"" + postpackage + "\","
						+ "\"token\":\"" + getToken() + "\","
						+ "\"sign\":\"" + sign + "\"}").getBytes();
				String result = httpPost("/server/checkLTPhone", data);
				System.out.println(result);
				JSONObject jsonObject=JSONObject.fromObject(result);
				String code=jsonObject.getString("code");
				CheckResponse response=new CheckResponse();
				response.setCode(code);
				if("000".equals(code)){
					response.setDesc(jsonObject.getJSONObject("data").getString("desc"));
				}
				return response;
			} catch (NoSuchAlgorithmException | IOException e) {
				return null;
			}
		}

		public String getToken() throws IOException, NoSuchAlgorithmException {
			TokenRequest tokenRequest = new TokenRequest();
			tokenRequest.setAppkey(Constants.LIUMI_APPKEY);
			String md5AppSecret = MD5Util.string2MD5(Constants.LIUMI_APPSECRET);
			tokenRequest.setAppsecret(md5AppSecret);
			String sign = SHA1.getSHA1(tokenRequest.toSign());
			byte[] data = ("{\"appkey\":\"" + Constants.LIUMI_APPKEY
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
			URL httpUrl = new URL(Constants.LIUMI_HOST + url);
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
		public static void main(String[] args) {
			Constants.LIUMI_HOST="http://api.tenchang.com";
			Constants.LIUMI_APPKEY="ydDlnIZFLW";
			Constants.LIUMI_APPSECRET="u6uz4kA4X8mxbqUB";
			new ApiService().placeOrder(1l, "YD500", "13880378674");
		}
}
