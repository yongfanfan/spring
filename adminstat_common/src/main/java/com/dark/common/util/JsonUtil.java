package com.dark.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import com.dark.common.domain.Constants;

public class JsonUtil {

	private static final Logger logger = Logger
			.getLogger(JsonUtil.class);
	private static final ObjectMapper jacksonObjectMapper = new ObjectMapper();

	public static String writeValueAsString(Object value) {
		if (value == null) {
			return null;
		}
		String result = null;
		try {
			result = jacksonObjectMapper.writeValueAsString(value);
		} catch (JsonGenerationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could't generation Object " + value.getClass(), e);
			}
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Generated value " + value + " result is " + result);
		}
		return result;
	}

	public static <T> T readValueWithString(String content, Class<T> valueType) {
		if (content == null) {
			return null;
		}
		T result = null;
		try {
			result = jacksonObjectMapper.readValue(content, valueType);
		} catch (JsonParseException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could't parse Object " + content, e);
			}
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static <T> T readValueWithString(String content,
			TypeReference valueTypeRef) {
		if (content == null) {
			return null;
		}
		T result = null;
		try {
			result = jacksonObjectMapper.readValue(content, valueTypeRef);
		} catch (JsonParseException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could't parse Object " + content, e);
			}
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String writeWithoutAnnoatioin(Object value) {
		if (value == null) {
			return null;
		}
		String result = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS,
					false);
			objectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE,
					false);
			objectMapper.getSerializationConfig().setSerializationInclusion(
					JsonSerialize.Inclusion.NON_NULL);
			objectMapper.configure(
					SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
			objectMapper.setDateFormat(new SimpleDateFormat(
					Constants.RFC3339_FORMAT));
			result = objectMapper.writeValueAsString(value);
		} catch (JsonGenerationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could't generation Object " + value.getClass(), e);
			}
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Generated value " + value + " result is " + result);
		}
		return result;
	}
	
}
