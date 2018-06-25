package com.dark.api.exception.handler;

import org.springframework.http.ResponseEntity;

import com.dark.api.base.Response;

public interface WebResponseExceptionTranslator {

	ResponseEntity<Response> translate(Exception e) throws Exception;

}
