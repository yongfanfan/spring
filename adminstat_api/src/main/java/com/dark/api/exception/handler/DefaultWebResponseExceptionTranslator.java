package com.dark.api.exception.handler;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.ThrowableAnalyzer;

import com.dark.api.base.ErrorCode;
import com.dark.api.base.Response;
import com.dark.api.exception.ClientException;
import com.dark.api.exception.InvalidUserException;
import com.dark.api.exception.ServerErrorException;
import com.dark.api.exception.UserDeniedAuthorizationException;
import com.dark.common.domain.Constants;

public class DefaultWebResponseExceptionTranslator implements
		WebResponseExceptionTranslator {

	private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

	public ResponseEntity<Response> translate(Exception e) throws Exception {

		Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
		RuntimeException ase = (ClientException) throwableAnalyzer
				.getFirstThrowableOfType(ClientException.class, causeChain);

		if (ase != null) {
			return handleClientException((ClientException) ase);
		}

		ase = (AuthenticationException) throwableAnalyzer
				.getFirstThrowableOfType(AuthenticationException.class,
						causeChain);
		if (ase != null) {
			if(ase instanceof AuthenticationServiceException) {
				return handleClientException(new ServerErrorException(e.getMessage(),e));
			}
			return handleClientException(new InvalidUserException(
					e.getMessage(), e));
		}

		ase = (AccessDeniedException) throwableAnalyzer
				.getFirstThrowableOfType(AccessDeniedException.class,
						causeChain);
		if (ase instanceof AccessDeniedException) {
			return handleClientException(new UserDeniedAuthorizationException(
					ase.getMessage(), ase));
		}

		if (Constants.IS_DEBUG) {
			return handleClientException(new ServerErrorException(
					e.getMessage(), e));
		} else {
			return handleClientException(new ServerErrorException(
					ErrorCode.SERVER_ERROR.message()));
		}

	}

	private ResponseEntity<Response> handleClientException(ClientException e)
			throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cache-Control", "no-store");
		headers.set("Pragma", "no-cache");
		ResponseEntity<Response> responseEntity = new ResponseEntity<Response>(
				Response.error(new com.dark.api.base.Error(e,e.code(),Constants.IS_DEBUG)), headers, HttpStatus.BAD_REQUEST);
		return responseEntity;

	}

	public void setThrowableAnalyzer(ThrowableAnalyzer throwableAnalyzer) {
		this.throwableAnalyzer = throwableAnalyzer;
	}

}
