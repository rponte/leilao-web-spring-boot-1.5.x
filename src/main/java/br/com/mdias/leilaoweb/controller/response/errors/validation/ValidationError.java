package br.com.mdias.leilaoweb.controller.response.errors.validation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationError {

	private String field;
	private String message;
	
	@JsonCreator
	public ValidationError(@JsonProperty("field") String field, @JsonProperty("message") String message) {
		this.field = field;
		this.message = message;
	}

	public String getField() {
		return field;
	}
	public String getMessage() {
		return message;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidationError other = (ValidationError) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ValidationError [field=" + field + ", message=" + message + "]";
	}
	
}
