package com.dark.api.json.serializer;

import java.io.IOException;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.dark.common.domain.Constants;

public class ResourceUrlsSerializer extends JsonSerializer<String[]> {

	@Override
	public void serialize(String[] value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartArray();
		for (String url : value) {
			Matcher matcher = ResourceUrlSerializer.URL_REG_PATTERN
					.matcher(url);
			if (matcher.matches()) {
				jgen.writeString(url);
			} else {
				url = StringUtils.startsWith(url, "/") ? url : "/" + value;
				jgen.writeString(Constants.RESOURCE_ROOT_URL + url);
			}
		}
		jgen.writeEndArray();
	}
}
