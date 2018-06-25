package com.dark.data.json.serializer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.dark.common.domain.Constants;

public class ResourceUrlSerializer extends JsonSerializer<String> {

	public static final Pattern URL_REG_PATTERN = Pattern
			.compile("^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=()]*)?$");

	@Override
	public void serialize(String value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		Matcher matcher = URL_REG_PATTERN.matcher(value);
		if (matcher.matches()) {
			jgen.writeString(value);
		} else {
			jgen.writeString(Constants.RESOURCE_ROOT_URL + value);
		}
	}
}
