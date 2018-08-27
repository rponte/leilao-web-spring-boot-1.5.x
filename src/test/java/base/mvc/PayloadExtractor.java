package base.mvc;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.jayway.jsonpath.JsonPath;

/**
 * https://www.baeldung.com/jackson-object-mapper-tutorial
 */
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
		Object data = jsonPath.read(body);
		
		T payload = jsonMapper.convertValue(data, payloadClass);
		return payload;
	}

	public <T> List<T> asListOf(Class<T> payloadClass) throws UnsupportedEncodingException {
		
		String body = result.getResponse().getContentAsString();
		
		JsonPath jsonPath = JsonPath.compile("@.data");
		Object data = jsonPath.read(body);
		
		CollectionType listType = jsonMapper.getTypeFactory().constructCollectionType(List.class, payloadClass);
		List<T> payload = jsonMapper.convertValue(data, listType);
		return payload;
	}
	
}
