package com.dark.api.exception.handler;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.dark.api.base.ErrorCode;
import com.dark.api.base.Response;
import com.dark.api.exception.InvalidTokenException;

public abstract class AbstractClientSecurityExceptionHandler {

	protected final Log logger = LogFactory.getLog(getClass());

	private WebResponseExceptionTranslator exceptionTranslator = new DefaultWebResponseExceptionTranslator();

	private ClientExceptionRenderer exceptionRenderer = new DefaultClientExceptionRenderer();

	private HandlerExceptionResolver handlerExceptionResolver = new DefaultHandlerExceptionResolver();
	
	private String errorPage = "/fail";

	public void setExceptionTranslator(
			WebResponseExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	public void setExceptionRenderer(ClientExceptionRenderer exceptionRenderer) {
		this.exceptionRenderer = exceptionRenderer;
	}

	protected final void doHandle(HttpServletRequest request,
			HttpServletResponse response, Exception authException)
			throws IOException, ServletException {
		try {
			if (request.getRequestURI().contains("/game") || request.getRequestURI().contains("deposit/withdraw")) {
				//处理网页请求token异常
				if (!response.isCommitted()) {
		            if (errorPage != null) {
		                // Put exception into request scope (perhaps of use to a view)
//		                request.setAttribute(WebAttributes.ACCESS_DENIED_403, authException);
		            	String error = null;
		            	if (authException.getCause() instanceof InvalidTokenException) {
		            		error = ErrorCode.INVALID_TOKEN.message();
		            	} else {
		            		error = "请求错误";
		            	}
		                // Set the 403 status code.
		                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		                // forward to error page.
		                RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage+"?error="+error);
		                dispatcher.forward(request, response);
		            } else {
		                response.sendError(HttpServletResponse.SC_FORBIDDEN, authException.getMessage());
		            }
		        }
				return;
			}
			ResponseEntity<Response> result = exceptionTranslator
					.translate(authException);
			result = enhanceResponse(result, authException);
			exceptionRenderer.handleHttpEntityResponse(result,
					new ServletWebRequest(request, response));
			response.flushBuffer();
		} catch (ServletException e) {
			if (handlerExceptionResolver.resolveException(request, response,
					this, e) == null) {
				throw e;
			}
		} catch (IOException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected ResponseEntity<Response> enhanceResponse(
			ResponseEntity<Response> result, Exception authException) {
		return result;
	}

}
