package com.dark.api.exception.handler;

import org.springframework.http.HttpEntity;
import org.springframework.web.context.request.ServletWebRequest;

public interface ClientExceptionRenderer {
	void handleHttpEntityResponse(HttpEntity<?> responseEntity,
			ServletWebRequest webRequest) throws Exception;
}
