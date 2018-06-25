package com.dark.api.exception.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.dark.api.base.ErrorCode;
import com.dark.api.exception.ClientException;
import com.dark.api.exception.InvalidMethodException;
import com.dark.api.exception.InvalidPageException;
import com.dark.api.exception.InvalidRequestException;
import com.dark.api.exception.InvalidTokenException;
import com.dark.api.exception.InvalidUserException;
import com.dark.api.exception.ServerErrorException;
import com.dark.api.exception.UserDeniedAuthorizationException;
import com.dark.api.exception.ValidatorException;
import com.dark.common.validator.BeanValidators;
import com.google.common.collect.Maps;

public class RestApiExceptionResolver extends AbstractHandlerExceptionResolver {

	protected final Log logger = LogFactory.getLog(RestApiExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ResponseBody responseBodyAnn = null;
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			responseBodyAnn = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResponseBody.class);
		}
		if (responseBodyAnn == null) {  
			return new ModelAndView("fail",doResolveExceptionWithModelView(ex));
		}
		if (ex instanceof ClientException) {
			return null;
		}
		if (ex instanceof NoSuchRequestHandlingMethodException) {
			throw new InvalidPageException(ex.getMessage(), ex);
		} else if (ex instanceof HttpRequestMethodNotSupportedException) {
			throw new InvalidMethodException(ex.getMessage(), ex);
		} else if (ex instanceof HttpMediaTypeNotSupportedException) {
			throw new ServerErrorException(ex.getMessage(), ex);
		} else if (ex instanceof HttpMediaTypeNotAcceptableException) {
			throw new ServerErrorException(ex.getMessage(), ex);
		} else if (ex instanceof MissingServletRequestParameterException) {
			throw new InvalidRequestException(ex.getMessage(), ex);
		} else if (ex instanceof ServletRequestBindingException) {
			throw new InvalidRequestException(ex.getMessage(), ex);
		} else if (ex instanceof ConversionNotSupportedException) {
			throw new ServerErrorException(ex.getMessage(), ex);
		} else if (ex instanceof TypeMismatchException) {
			throw new InvalidRequestException(ex.getMessage(), ex);
		} else if (ex instanceof HttpMessageNotReadableException) {
			throw new ServerErrorException(ex.getMessage(), ex);
		} else if (ex instanceof HttpMessageNotWritableException) {
			throw new ServerErrorException(ex.getMessage(), ex);
		} else if (ex instanceof MethodArgumentNotValidException) {
			throw new ServerErrorException(ex.getMessage(), ex);
		} else if (ex instanceof MissingServletRequestPartException) {
			throw new ServerErrorException(ex.getMessage(), ex);
		} else if (ex instanceof BindException) {
			throw new InvalidRequestException(ex.getMessage(), ex);
		} else if (ex instanceof MissingServletRequestParameterException) {
			throw new InvalidRequestException(ex.getMessage(), ex);
		} else if (ex instanceof BadCredentialsException) {
			throw new InvalidUserException(ex.getMessage(), ex);
		} else if (ex instanceof AuthenticationCredentialsNotFoundException) {
			throw new UserDeniedAuthorizationException(ex.getMessage());
		} else if (ex instanceof ConstraintViolationException) {
			ConstraintViolationException ce = (ConstraintViolationException) ex;
//			throw new ValidatorException(Collections3.convertToString(BeanValidators.extractMessage(ce), ","));
			throw new ValidatorException((BeanValidators.extractMessage(ce)).get(0));
		} else {
			if (ex != null) {
				throw new ServerErrorException(ex.getMessage(), ex);
			}
		}
		return null;
	}
	
	private Map<String,Object> doResolveExceptionWithModelView(Exception ex) {
		HashMap<String,Object> map = Maps.newHashMap();
		if (ex instanceof AuthenticationCredentialsNotFoundException) {
			map.put("error", ErrorCode.USER_DENIED.message());
		} else if (ex instanceof InvalidTokenException) {
			map.put("error", ErrorCode.INVALID_TOKEN.message());
		} else if (ex instanceof AccessDeniedException) {
			map.put("error", ErrorCode.USER_DENIED.message());
		} else {
			map.put("error", "请求错误");
		}
		return map;
		
	}
}
