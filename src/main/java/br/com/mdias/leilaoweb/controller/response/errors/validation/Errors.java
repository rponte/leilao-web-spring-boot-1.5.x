package br.com.mdias.leilaoweb.controller.response.errors.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Representa o conjunto de erros de uma entidade ou formulario
 */
public class Errors {

	private List<ValidationError> errors = new ArrayList<>();
	
	/**
	 * Registra novo erro para determinado campo
	 */
	public Errors with(String field, String message) {
		errors.add(new ValidationError(field, message));
		return this;
	}
	
	public List<ValidationError> getErrors() {
		return errors;
	}

	public static Errors of(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
		return Errors.of(violations);
	}
	
	public static Errors of(Collection<ConstraintViolation<?>> violations) {
		
		Errors errors = new Errors();
		for (ConstraintViolation<?> violation : violations) {
	    	String field = violation.getPropertyPath().toString();
	    	String message = violation.getMessage();
	        errors
	        	.with(field, message);
	    }
		
		return errors;
	}

	public static Errors of(BindingResult result, Locale locale, MessageSource i18n) {
		List<FieldError> fieldErrors = result.getFieldErrors();
		return Errors.of(fieldErrors, locale, i18n);
	}
	
	public static Errors of(List<FieldError> fieldErrors, Locale locale, MessageSource i18n) {
		
		Errors errors = new Errors();
		for (FieldError fieldError : fieldErrors) {
			String field = fieldError.getField();
			String message = i18n.getMessage(fieldError, locale);
			errors
				.with(field, message);
		}
		
		return errors;
	}
	
}
