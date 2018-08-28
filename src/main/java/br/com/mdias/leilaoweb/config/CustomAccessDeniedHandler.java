package br.com.mdias.leilaoweb.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mdias.leilaoweb.controller.response.jsend.Json;
import br.com.mdias.leilaoweb.controller.response.jsend.JsonResult;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
	
	private final ObjectMapper jsonMapper;
	
	public CustomAccessDeniedHandler(ObjectMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			logger.warn("User '{}' attempted to access the protected URL '{}' ", auth.getName(), request.getRequestURI());
		}
		
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonResult(accessDeniedException));
		
	}

	private String jsonResult(AccessDeniedException exception) throws JsonProcessingException {
		
		JsonResult json = Json.fail()
							  .withMessage(exception.getLocalizedMessage())
							  .build();
		
		return jsonMapper.writeValueAsString(json);
	}

}
