package br.com.mdias.leilaoweb.controller.response.errors.validation;

import java.util.ArrayList;
import java.util.List;

public class Errors {

	private List<ValidationError> errors = new ArrayList<>();
	
	public Errors with(String field, String message) {
		errors.add(new ValidationError(field, message));
		return this;
	}
	
	public List<ValidationError> getErrors() {
		return errors;
	}
	
}
