package com.dark.api.mvc.adapter;

import java.io.IOException;
import java.lang.reflect.Method;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.dark.api.base.Response;

public class CustomServletInvocableHandlerMethod extends InvocableHandlerMethod {

	private HttpStatus responseStatus;

	private String responseReason;

	private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

	public void setHandlerMethodReturnValueHandlers(
			HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
		this.returnValueHandlers = returnValueHandlers;
	}

	public CustomServletInvocableHandlerMethod(Object handler, Method method) {
		super(handler, method);

		ResponseStatus annotation = getMethodAnnotation(ResponseStatus.class);
		if (annotation != null) {
			this.responseStatus = annotation.value();
			this.responseReason = annotation.reason();
		}
	}

	public final void invokeAndHandle(NativeWebRequest request,
			ModelAndViewContainer mavContainer, Object... providedArgs)
			throws Exception {

		Object returnValue = invokeForRequest(request, mavContainer,
				providedArgs);
		if (!(returnValue instanceof Response)
				&& !(returnValue instanceof ResponseEntity<?>)
				&& !(returnValue instanceof ModelAndView)) {
			returnValue = Response.success(returnValue);
		}
		setResponseStatus((ServletWebRequest) request);

		if (returnValue == null) {
			if (isRequestNotModified(request) || hasResponseStatus()
					|| mavContainer.isRequestHandled()) {
				mavContainer.setRequestHandled(true);
				return;
			}
		}

		mavContainer.setRequestHandled(false);

		try {
			returnValueHandlers.handleReturnValue(returnValue, getReturnType(),
					mavContainer, request);
		} catch (Exception ex) {
			if (logger.isTraceEnabled()) {
				logger.trace(
						getReturnValueHandlingErrorMessage(
								"Error handling return value", returnValue), ex);
			}
			throw ex;
		}
	}

	private String getReturnValueHandlingErrorMessage(String message,
			Object returnValue) {
		StringBuilder sb = new StringBuilder(message);
		if (returnValue != null) {
			sb.append(" [type=" + returnValue.getClass().getName() + "] ");
		}
		sb.append("[value=" + returnValue + "]");
		return getDetailedErrorMessage(sb.toString());
	}

	private void setResponseStatus(ServletWebRequest webRequest)
			throws IOException {
		if (this.responseStatus != null) {
			if (StringUtils.hasText(this.responseReason)) {
				webRequest.getResponse().sendError(this.responseStatus.value(),
						this.responseReason);
			} else {
				webRequest.getResponse().setStatus(this.responseStatus.value());
			}

			webRequest.getRequest().setAttribute(
					View.RESPONSE_STATUS_ATTRIBUTE, this.responseStatus);
		}
	}

	private boolean isRequestNotModified(NativeWebRequest request) {
		return ((ServletWebRequest) request).isNotModified();
	}

	private boolean hasResponseStatus() {
		return responseStatus != null;
	}
}
