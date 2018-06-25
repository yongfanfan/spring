package com.dark.common.json;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.stereotype.Component;

import com.dark.common.domain.Constants;

@Component
public class JacksonObjectMapper extends ObjectMapper {
	@SuppressWarnings("deprecation")
	public JacksonObjectMapper() {
		super();
		if (Constants.IS_DEBUG) {
			//格式化输出json
			configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		}
		configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
		//为空的不输出
		getSerializationConfig().setSerializationInclusion(
				JsonSerialize.Inclusion.NON_NULL);
		configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		setDateFormat(new SimpleDateFormat(Constants.RFC3339_FORMAT));
		/*this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {

			@Override
			public void serialize(Object value, JsonGenerator jgen,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				jgen.writeString("");//空值设为空字符串
			}
		});*/
	}

}
