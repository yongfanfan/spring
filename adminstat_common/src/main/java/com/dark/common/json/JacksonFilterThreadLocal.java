package com.dark.common.json;

import org.springframework.core.NamedThreadLocal;

import com.dark.common.annoation.JacksonFilters;

public class JacksonFilterThreadLocal {
	private static NamedThreadLocal<JacksonFilters> jacksonFilterThreadLocal = new NamedThreadLocal<JacksonFilters>("jacksonFilter");

	public static void setJacksonFilters(JacksonFilters jacksonFilters) {
		jacksonFilterThreadLocal.set(jacksonFilters);
	}

	public static JacksonFilters getJacksonFilters() {
		JacksonFilters filters = jacksonFilterThreadLocal.get();
		return filters;
	}

	public static void removeJacksonFilters() {
		jacksonFilterThreadLocal.remove();
	}
}
