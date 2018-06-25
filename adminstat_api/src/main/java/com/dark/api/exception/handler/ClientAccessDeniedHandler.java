package com.dark.api.exception.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class ClientAccessDeniedHandler extends
		AbstractClientSecurityExceptionHandler implements AccessDeniedHandler {

	public void handle(HttpServletRequest request,
			HttpServletResponse response, AccessDeniedException authException)
			throws IOException, ServletException {
		doHandle(request, response, authException);
	}

}
