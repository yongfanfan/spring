package com.dark.api.json.serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.util.JSONPObject;

import com.dark.api.base.Response;
import com.dark.common.domain.Constants;

public class ResponseSerializer extends JsonSerializer<Response> {

	@Override
	public void serialize(Response value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		if(StringUtils.isEmpty(value.getCallback())){
			if(value.getStatus()==0){
				jgen.writeObject(value.getResult());
			}
			else{
				jgen.writeStartObject();
				jgen.writeObjectField("status", value.getStatus());
				if (value.getError() != null) {
					jgen.writeObjectField("data", value.getError().getMessage());
					if(Constants.IS_DEBUG) {//错误堆栈信息，调试用
						jgen.writeObjectField("errorStack", value.getError().getStack());
					}
				}
				if (value.getPager() != null) {
					jgen.writeObjectField("count",value.getPager().getTotalCount());
				}
				if (value.getResult() != null) {
					jgen.writeObjectField("data", value.getResult());
				} else if (value.getError() == null) {
					jgen.writeFieldName("data");
					jgen.writeStartObject();
					jgen.writeEndObject();
				}
				jgen.writeEndObject();
				}
		}
		else{
			Map<String,Object> result=new HashMap<String,Object>();
			result.put("status", value.getStatus());
			if (value.getError() != null) {
				result.put("data", value.getError().getMessage());
				if(Constants.IS_DEBUG) {//错误堆栈信息，调试用
					result.put("errorStack", value.getError().getStack());
				}
			}
			if (value.getPager() != null) {
				result.put("count",value.getPager().getTotalCount());
			}
			if (value.getResult() != null) {
				result.put("data", value.getResult());
			} else if (value.getError() == null) {
				result.put("data","{}");
			}
			jgen.writeObject(new JSONPObject(value.getCallback(), result));
		}
	}
}
