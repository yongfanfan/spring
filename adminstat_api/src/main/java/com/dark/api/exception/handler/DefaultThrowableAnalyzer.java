package com.dark.api.exception.handler;

import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableCauseExtractor;

import javax.servlet.ServletException;

public final class DefaultThrowableAnalyzer extends ThrowableAnalyzer {

	protected void initExtractorMap() {
		super.initExtractorMap();
		registerExtractor(ServletException.class,
				new ThrowableCauseExtractor() {
					public Throwable extractCause(Throwable throwable) {
						ThrowableAnalyzer.verifyThrowableHierarchy(throwable,
								ServletException.class);
						return ((ServletException) throwable).getRootCause();
					}
				});
	}
}
