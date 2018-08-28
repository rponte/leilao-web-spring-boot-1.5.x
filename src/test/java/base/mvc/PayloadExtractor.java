package base.mvc;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.jayway.jsonpath.JsonPath;

/**
 * Result Handler utilizado para extrair o payload do <code>JsonResult</code>.
 * Basicamente ele guarda o resultado da requisição e permite que, em seguida,
 * se extraia o json do nó <code>@.data</code> convertendo-o para um tipo
 * informado.
 * 
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
		Object data = extractDataPayloadFromHttpBody();
		T payload = jsonMapper.convertValue(data, payloadClass);
		return payload;
	}

	/**
	 * Extracts payload <code>@.data</code> from HTTP body and convert it to a
	 * collection of instances of <code>payloadClass</code> type
	 */
	public <T> List<T> asListOf(Class<T> payloadClass) throws UnsupportedEncodingException {
		
		Object data = extractDataPayloadFromHttpBody();
		
		CollectionType listType = jsonMapper.getTypeFactory().constructCollectionType(List.class, payloadClass);
		List<T> payload = jsonMapper.convertValue(data, listType);
		return payload;
	}
	
	private Object extractDataPayloadFromHttpBody() throws UnsupportedEncodingException {
		String body = result.getResponse().getContentAsString();
		JsonPath jsonPath = JsonPath.compile("@.data");
		Object data = jsonPath.read(body);
		return data;
	}
	
}
