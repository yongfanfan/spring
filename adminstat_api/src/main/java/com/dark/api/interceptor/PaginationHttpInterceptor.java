package com.dark.api.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dark.api.annoation.Pagination;
import com.dark.common.domain.Pager;
import com.dark.common.localthread.PaginationThreadLocal;

public class PaginationHttpInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = Logger.getLogger(PaginationHttpInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Integer count = getPagerParam(request, "pageSize");
		Integer startIndex = getPagerParam(request, "startIndex");
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("to begin handle paginate,params is:pageSize %s,startIndex %s",count,startIndex));
		}
		if (count > 0) {
			PaginationThreadLocal.setPager(new Pager(count, startIndex));
			return super.preHandle(request, response, handler);
		}
		if (startIndex > 0) {
			PaginationThreadLocal.setPager(new Pager(startIndex));
			return super.preHandle(request, response, handler);
		}
		if (handler instanceof HandlerMethod) {
			Method method = ((HandlerMethod) handler).getMethod();
			boolean isNeedPager = method.isAnnotationPresent(Pagination.class);
			if (isNeedPager) {
				Pagination needPager = method.getAnnotation(Pagination.class);
				PaginationThreadLocal.setPager(new Pager(count > 0 ? count
						: needPager.count(), startIndex > 0 ? startIndex
						: needPager.startIndex()));
			}
		}
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
		PaginationThreadLocal.removePager();
		if (logger.isDebugEnabled()) {
			logger.debug("complet handling paginate plugin");
		}
		super.afterCompletion(request, response, handler, ex);
	}

	private Integer getPagerParam(HttpServletRequest request, String key) {
		try {
			String param = request.getParameter(key);
			return Integer.parseInt(param == null ? "0" : param);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
