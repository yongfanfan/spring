package com.dark.api.exception.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dark.api.base.Response;

@Component
public class ClientAuthenticationEntryPoint extends
		AbstractClientSecurityExceptionHandler implements
		AuthenticationEntryPoint {

	private String realmName = "shaketv-api";

	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}

	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		doHandle(request, response, authException);
	}

	@Override
	protected ResponseEntity<Response> enhanceResponse(
			ResponseEntity<Response> response, Exception exception) {
		HttpHeaders headers = response.getHeaders();
		String existing = null;
		if (headers.containsKey("WWW-Authenticate")) {
			existing = extractTypePrefix(headers.getFirst("WWW-Authenticate"));
		}
		StringBuilder builder = new StringBuilder();
		builder.append("realm=\"" + realmName + "\"");
		if (existing != null) {
			builder.append(", " + existing);
		}
		HttpHeaders update = new HttpHeaders();
		update.putAll(response.getHeaders());
		update.set("WWW-Authenticate", builder.toString());
		return new ResponseEntity<Response>(response.getBody(), update,
				response.getStatusCode());
	}

	private String extractTypePrefix(String header) {
		String existing = header;
		String[] tokens = existing.split(" +");
		if (tokens.length > 1 && !tokens[0].endsWith(",")) {
			existing = StringUtils.arrayToDelimitedString(tokens, " ")
					.substring(existing.indexOf(" ") + 1);
		}
		return existing;
	}
}
