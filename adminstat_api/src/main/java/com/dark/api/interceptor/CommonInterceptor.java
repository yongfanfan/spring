package com.dark.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dark.common.annoation.JacksonFilters;
import com.dark.common.json.JacksonFilterThreadLocal;

public class CommonInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = Logger.getLogger(CommonInterceptor.class);

	private NamedThreadLocal<Long> timmer = new NamedThreadLocal<Long>(
			"StopWatch");

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		long process_start_time = System.currentTimeMillis();
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			JacksonFilters jacksonFilters = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), JacksonFilters.class);
			JacksonFilterThreadLocal.setJacksonFilters(jacksonFilters);
		}
		timmer.set(process_start_time);
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long process_end_time = System.currentTimeMillis();
		long process_start_time = timmer.get();
		long process_time = process_end_time - process_start_time;
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("%s cost %d millis",
					request.getRequestURI(), process_time));
		}
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

}
