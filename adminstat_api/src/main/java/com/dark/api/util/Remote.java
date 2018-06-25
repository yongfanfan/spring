package com.dark.api.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;

public class Remote {
	public static String POSTMethod(String url, Map<String, Object> argsMap,
			String content) throws Exception {
		byte[] dataByte = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		if (MapUtils.isNotEmpty(argsMap)) {
			UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(
					setHttpParams(argsMap), "utf-8");
			httpPost.setEntity(encodedFormEntity);
		}
		if (StringUtils.isNotEmpty(content)) {
			httpPost.setEntity(new ByteArrayEntity(content.getBytes("utf-8")));
		}

		HttpResponse httpResponse = httpClient.execute(httpPost);

		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpPost.abort();
		}

		String result = bytesToString(dataByte);
		return result;
	}

	public static String POSTMethod(String url, Map<String, Object> argsMap,
			String content, String cookies) throws Exception {
		byte[] dataByte = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		if (MapUtils.isNotEmpty(argsMap)) {
			UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(
					setHttpParams(argsMap), "UTF-8");
			httpPost.setEntity(encodedFormEntity);
		}
		if (StringUtils.isNotEmpty(content)) {
			httpPost.setEntity(new ByteArrayEntity(content.getBytes()));
		}

		if (StringUtils.isNotEmpty(cookies)) {
			httpPost.setHeader("Cookie", cookies);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Type", "application/json");
		}

		HttpResponse httpResponse = httpClient.execute(httpPost);

		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpPost.abort();
		}

		String result = bytesToString(dataByte);
		return result;
	}

	public static String GETMethod(String url, Map<String, Object> argsMap)
			throws Exception {
		byte[] dataByte = null;
		HttpClient httpClient = new DefaultHttpClient();

		url = formatGetParameter(url, argsMap);
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpGet.abort();
		}

		String result = bytesToString(dataByte);
		return result;
	}

	public static String PUTMethod(String url, Map<String, Object> argsMap,
			String cookies, String content) throws Exception {
		byte[] dataByte = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(url);

		if (StringUtils.isNotEmpty(content)) {
			httpPut.setEntity(new ByteArrayEntity(content.getBytes()));
		}

		if (StringUtils.isNotEmpty(cookies)) {
			httpPut.setHeader("Cookie", cookies);
			httpPut.setHeader("Accept", "application/json");
			httpPut.setHeader("Content-Type", "application/json");
		}

		if (MapUtils.isNotEmpty(argsMap)) {
			UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(
					setHttpParams(argsMap), "UTF-8");
			httpPut.setEntity(encodedFormEntity);
		}

		HttpResponse httpResponse = httpClient.execute(httpPut);

		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpPut.abort();
		}

		String result = bytesToString(dataByte);
		return result;
	}

	public static String PUTMethod(String url, Map<String, Object> argsMap,
			Map<String, String> headerParam, String content) throws Exception {
		byte[] dataByte = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(url);

		if (StringUtils.isNotEmpty(content)) {
			httpPut.setEntity(new ByteArrayEntity(content.getBytes()));
		}

		if (MapUtils.isNotEmpty(headerParam)) {
			Set entrySet = headerParam.entrySet();
			Iterator entryIter = entrySet.iterator();
			while (entryIter.hasNext()) {
				Map.Entry entry = (Map.Entry) entryIter.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				httpPut.setHeader(key, value);
			}
		}

		if (MapUtils.isNotEmpty(argsMap)) {
			UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(
					setHttpParams(argsMap), "UTF-8");
			httpPut.setEntity(encodedFormEntity);
		}

		HttpResponse httpResponse = httpClient.execute(httpPut);

		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpPut.abort();
		}

		String result = bytesToString(dataByte);
		return result;
	}

	public static String DELETEMethod(String url, Map<String, Object> argsMap,
			Map<String, String> headerParam) throws Exception {
		byte[] dataByte = null;
		url = formatGetParameter(url, argsMap);
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(url);

		if (MapUtils.isNotEmpty(headerParam)) {
			Set entrySet = headerParam.entrySet();
			Iterator entryIter = entrySet.iterator();
			while (entryIter.hasNext()) {
				Map.Entry entry = (Map.Entry) entryIter.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				httpDelete.setHeader(key, value);
			}
		}

		HttpResponse httpResponse = httpClient.execute(httpDelete);

		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpDelete.abort();
		}

		String result = bytesToString(dataByte);
		return result;
	}

	public static String formatGetParameter(String url,
			Map<String, Object> argsMap) {
		if ((url != null) && (url.length() > 0)) {
			if (!url.endsWith("?")) {
				url = url + "?";
			}
			if ((argsMap != null) && (!argsMap.isEmpty())) {
				Set entrySet = argsMap.entrySet();
				Iterator iterator = entrySet.iterator();
				while (iterator.hasNext()) {
					Map.Entry entry = (Map.Entry) iterator.next();
					if (entry != null) {
						String key = (String) entry.getKey();
						String value = (String) entry.getValue();
						url = url + key + "=" + value;
						if (iterator.hasNext()) {
							url = url + "&";
						}
					}
				}
			}
		}
		return url;
	}

	public static byte[] getData(HttpEntity httpEntity) throws Exception {
		BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
				httpEntity);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bufferedHttpEntity.writeTo(byteArrayOutputStream);
		byte[] responseBytes = byteArrayOutputStream.toByteArray();
		return responseBytes;
	}

	private static List<BasicNameValuePair> setHttpParams(
			Map<String, Object> argsMap) {
		List nameValuePairList = new ArrayList();

		if ((argsMap != null) && (!argsMap.isEmpty())) {
			Set set = argsMap.entrySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				BasicNameValuePair basicNameValuePair = new BasicNameValuePair(
						(String) entry.getKey(), entry.getValue().toString());
				nameValuePairList.add(basicNameValuePair);
			}
		}
		return nameValuePairList;
	}

	public static String bytesToString(byte[] bytes)
			throws UnsupportedEncodingException {
		if (bytes != null) {
			String returnStr = new String(bytes, "utf-8");
			returnStr = StringUtils.trim(returnStr);
			return returnStr;
		}
		return null;
	}
	public static String POSTMethodWithHttps(String url,Map<String,Object> argsMap,String content) throws Exception{
		byte[] dataByte=null;
		SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        };
        ctx.init(null, new TrustManager[] { tm }, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("https", 443, ssf));
        ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
		HttpClient httpClient = new DefaultHttpClient(mgr, new DefaultHttpClient().getParams());
		HttpPost httpPost = new HttpPost(url);
		if (MapUtils.isNotEmpty(argsMap)) {
			// 设置参数
			UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(setHttpParams(argsMap), "UTF-8");
			httpPost.setEntity(encodedFormEntity);
		}
		if (StringUtils.isNotEmpty(content)) {
			httpPost.setEntity(new ByteArrayEntity(content.getBytes()));
		}
		// 执行请求
		HttpResponse httpResponse = httpClient.execute(httpPost);
		// 获取返回的数据
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpPost.abort();
		}
		// 将字节数组转换成为字符串
		String result = bytesToString(dataByte);
		return result;
}
	public static String GETMethod(String url)
			throws Exception {
		byte[] dataByte = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpGet.abort();
		}

		String result = bytesToString(dataByte);
		return result;
	}
	public static String POSTMethod(String url,String content) throws Exception {
		byte[] dataByte = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		if (StringUtils.isNotEmpty(content)) {
			httpPost.setEntity(new ByteArrayEntity(content.getBytes("utf-8")));
		}

		HttpResponse httpResponse = httpClient.execute(httpPost);

		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null) {
			byte[] responseBytes = getData(httpEntity);
			dataByte = responseBytes;
			httpPost.abort();
		}

		String result = bytesToString(dataByte);
		return result;
	}
}
