package com.dark.common.json;

import java.io.IOException;
import java.nio.charset.Charset;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import com.dark.common.annoation.JacksonFilter;
import com.dark.common.annoation.JacksonFilters;

@Component
public class JacksonObjectMapperHttpMessageConvert extends
		AbstractHttpMessageConverter<Object> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private JacksonObjectMapper jacksonObjectMapper = new JacksonObjectMapper();

	public JacksonObjectMapperHttpMessageConvert() {
		super(new MediaType("application", "json", DEFAULT_CHARSET));
	}

	public JacksonObjectMapper getJacksonObjectMapper() {
		return jacksonObjectMapper;
	}

	public void setJacksonObjectMapper(JacksonObjectMapper jacksonObjectMapper) {
		this.jacksonObjectMapper = jacksonObjectMapper;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		JavaType javaType = getJavaType(clazz);
		return (this.jacksonObjectMapper.canDeserialize(javaType) && canRead(mediaType));
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return (this.jacksonObjectMapper.canSerialize(clazz) && canWrite(mediaType));
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		JavaType javaType = getJavaType(clazz);
		try {
			return this.jacksonObjectMapper.readValue(inputMessage.getBody(),
					javaType);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: "
					+ ex.getMessage(), ex);
		}
	}

	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		try {
			JacksonFilters jacksonFilters = JacksonFilterThreadLocal.getJacksonFilters();
			if (jacksonFilters != null) {
				SimpleFilterProvider filterProvider = new SimpleFilterProvider();
				JacksonFilter[] jfilters = jacksonFilters.value();
				for (JacksonFilter jacksonFilter : jfilters) {
					if (jacksonFilter.type() == JacksonFilter.FilterType.exclude) {
						filterProvider.addFilter(jacksonFilter.target(), SimpleBeanPropertyFilter
								.serializeAllExcept(jacksonFilter.properties()));
					} else {
						filterProvider.addFilter(jacksonFilter.target(), SimpleBeanPropertyFilter
								.filterOutAllExcept(jacksonFilter.properties()));
					}
				}
				this.jacksonObjectMapper.setFilters(filterProvider);
			}
			this.jacksonObjectMapper
					.writeValue(outputMessage.getBody(), object);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: "
					+ ex.getMessage(), ex);
		} finally {
			JacksonFilterThreadLocal.removeJacksonFilters();
			this.jacksonObjectMapper.setFilters(null);
		}
	}

	@SuppressWarnings("deprecation")
	protected JavaType getJavaType(Class<?> clazz) {
		return TypeFactory.type(clazz);
	}

}
