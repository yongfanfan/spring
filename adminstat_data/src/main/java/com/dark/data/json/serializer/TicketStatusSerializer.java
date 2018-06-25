package com.dark.data.json.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class TicketStatusSerializer extends JsonSerializer<String> {

	@Override
	public void serialize(String value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		if ("未开奖".equals(value)) {
			jgen.writeNumber(0);
		} else if ("已中奖".equals(value)) {
			jgen.writeNumber(2);
		} else if ("未中奖".equals(value)) {
			jgen.writeNumber(1);
		}  else {
			jgen.writeNumber(value);
		}
	}
}
