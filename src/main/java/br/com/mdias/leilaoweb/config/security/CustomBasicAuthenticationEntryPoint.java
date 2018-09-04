package br.com.mdias.leilaoweb.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mdias.leilaoweb.controller.response.jsend.Json;
import br.com.mdias.leilaoweb.controller.response.jsend.JsonResult;

/**
 * https://www.baeldung.com/spring-security-basic-authentication
 */
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(CustomBasicAuthenticationEntryPoint.class);
	
	private final ObjectMapper jsonMapper;
	
	public CustomBasicAuthenticationEntryPoint(ObjectMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			logger.warn("An unauthenticated user '{}' attempted to access the protected URL '{}' ", auth.getName(), request.getRequestURI());
		}
		
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonResult(authException));
	}
	
	private String jsonResult(AuthenticationException exception) throws JsonProcessingException {
		
		JsonResult<String> json = Json.fail()
				.withMessage(exception.getLocalizedMessage())
				.build();
		
		return jsonMapper.writeValueAsString(json);
	}
}
