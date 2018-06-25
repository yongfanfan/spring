package com.dark.api.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class CookieUtils {
	
	public static final Integer EXPIRE_12H = 12 * 60 * 60;

	public static void addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer maxAge, String path, String domain, Boolean secure) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);
		try {
			name = URLEncoder.encode(name, "UTF-8");
			value = URLEncoder.encode(value, "UTF-8");
			Cookie localCookie = new Cookie(name, value);
			if (maxAge != null)
				localCookie.setMaxAge(maxAge.intValue());
			if (StringUtils.isNotEmpty(path))
				localCookie.setPath(path);
			if (StringUtils.isNotEmpty(domain))
				localCookie.setDomain(domain);
			if (secure != null)
				localCookie.setSecure(secure.booleanValue());
			response.addCookie(localCookie);
		} catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
			localUnsupportedEncodingException1.printStackTrace();
		}
	}

	public static void addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer maxAge) {
		addCookie(request, response, name, value, maxAge,null, null,null);
	}

	public static void addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value) {
		addCookie(request, response, name, value, 86400,null, null,null);
	}

	public static String getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request);
		Assert.hasText(name);
		Cookie[] arrayOfCookie1 = request.getCookies();
		if (arrayOfCookie1 != null)
			try {
				name = URLEncoder.encode(name, "UTF-8");
				for (Cookie localCookie : arrayOfCookie1)
					if (name.equals(localCookie.getName()))
						return URLDecoder.decode(localCookie.getValue(),
								"UTF-8");
			} catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
				localUnsupportedEncodingException1.printStackTrace();
			}
		return null;
	}

	public static void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String path,
			String domain) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);
		try {
			name = URLEncoder.encode(name, "UTF-8");
			Cookie localCookie = new Cookie(name, null);
			localCookie.setMaxAge(0);
			if (StringUtils.isNotEmpty(path))
				localCookie.setPath(path);
			if (StringUtils.isNotEmpty(domain))
				localCookie.setDomain(domain);
			response.addCookie(localCookie);
		} catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
			localUnsupportedEncodingException1.printStackTrace();
		}
	}

	public static void removeCookie(HttpServletRequest request,
			HttpServletResponse response, String name) {
		removeCookie(request, response, name, null,null);
	}
}
