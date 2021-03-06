package br.com.mdias.leilaoweb.controller.response.jsend.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import br.com.mdias.leilaoweb.controller.response.jsend.JsonResult;
import br.com.mdias.leilaoweb.controller.response.jsend.Status;

/**
 * Interceptor responsável por setar o Http Status da requisição de acordo com o
 * status do <code>JsonResult</code> retornado pelo controller
 */
@ControllerAdvice
public class JSendResponseBodyAdvice implements ResponseBodyAdvice<JsonResult<?>> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return JsonResult.class.isAssignableFrom(returnType.getParameterType());
	}

	@Override
	public JsonResult<?> beforeBodyWrite(JsonResult<?> body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		
		response.setStatusCode(statusFor(body));
		return body;
	}

	/**
	 * Figures out the Http Status related to JSend status
	 */
	private HttpStatus statusFor(JsonResult<?> json) {
		Status jsendStatus = json.getStatus();
		return HttpStatus.valueOf(jsendStatus.getHttpStatusCode());
	}

}
