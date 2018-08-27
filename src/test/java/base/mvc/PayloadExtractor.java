package base.mvc;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

public class PayloadExtractor implements ResultHandler {

	private MvcResult result;
	private final ObjectMapper jsonMapper;
	
	public PayloadExtractor(ObjectMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void handle(MvcResult result) throws Exception {
		this.result = result;
	}

	/**
	 * Extracts payload <code>@.data</code> from HTTP body and convert it to a
	 * instance of <code>payloadClass</code> type
	 */
	public <T> T as(Class<T> payloadClass) throws UnsupportedEncodingException {
		
		String body = result.getResponse().getContentAsString();
		
		JsonPath jsonPath = JsonPath.compile("@.data");
		LinkedHashMap<String, Object> oops = jsonPath.read(body);
		
		T payload = jsonMapper.convertValue(oops, payloadClass);
		return payload;
	}
	
}
