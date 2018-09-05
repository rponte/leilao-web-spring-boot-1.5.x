package br.com.mdias.leilaoweb.controller.exceptions;

import java.util.List;
import java.util.Locale;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.mdias.leilaoweb.controller.response.errors.validation.Errors;
import br.com.mdias.leilaoweb.controller.response.jsend.Json;
import br.com.mdias.leilaoweb.controller.response.jsend.JsonResult;

/**
 * Exception Handler responsável por capturar as exceções lançadas pelo
 * Spring-MVC e as exceções não tratadas nos controllers, e tranformá-las em um
 * <code>JsonResult</code>.
 * 
 * <p> Para mais detalhes, https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
 */
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomRestExceptionHandler.class);
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Catches and handles all type of logic that deals with all other exceptions
	 * that don’t have specific handlers
	 */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		
		logger.error("Catching an unhandled exception thrown by a controller: " + ex.getLocalizedMessage(), ex);
		
	    Object body = null;
	    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();
		
		return handleExceptionInternal(ex, body, headers, status, request);
	}
	
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		
		Errors errors = new Errors();
	    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
	    	String field = violation.getPropertyPath().toString();
	    	String message = violation.getMessage();
	        errors
	        	.with(field, message);
	    }
		
		JsonResult<Errors> body = Json.fail()
							  .withMessage("Erro de validação")
							  .withData(errors)
							  .build();
		
	    HttpStatus status = HttpStatus.BAD_REQUEST;
		HttpHeaders headers = new HttpHeaders();
		
		return new ResponseEntity<Object>(body, headers, status);
	}
	
	/**
	 * Handling <code>AccessDeniedException</code> thrown by controllers, usually
	 * via <code>@Secure</code> annotation
	 */
	@ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException accessDenied, WebRequest request) {
		// re-throws exception so that Spring Security's AccessDeniedHandler can handle it
		throw accessDenied;
    }
	
	/**
	 * Customiza response de erro quando ocorrer erro de validação
	 * (<code>@Valid</code>) no controller
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		logger.error("Catching a validation error on '{}'", ex.getParameter().getMethod().toGenericString());
		
		Errors errors = new Errors();
		Locale locale = request.getLocale();
		
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			String field = fieldError.getField();
			String message = messageSource.getMessage(fieldError, locale);
			errors
				.with(field, message);
				;
		}
		
		JsonResult<Errors> body = Json.fail()
							  .withMessage("Erro de validação")
							  .withData(errors)
							  .build();
		
		return new ResponseEntity<Object>(body, headers, status);
	}
	
	/**
	 * Customiza Http body mas não altera Status code
	 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleExceptionInternal(java.lang.Exception, java.lang.Object, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (status.is4xxClientError()) {
			body = Json.fail()
				    .withMessage(ex.getLocalizedMessage())
				    .withData(body)
				    .build();
			
			return super.handleExceptionInternal(ex, body, headers, status, request);
		}
		
		body = Json.error()
			    .withMessage(ex.getLocalizedMessage())
			    .withData(body)
			    .build();
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
}
