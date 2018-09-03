package br.com.mdias.leilaoweb.controller.response.jsend;

/**
 * Based on jSend JSON Response - http://labs.omniti.com/labs/jsend
 */
public class JsonResult<T> {

	private Status status;
	private T data;
	
	private String message;
	private Integer code;
	
	public T getData() {
		return data;
	}
	public Status getStatus() {
		return status;
	}
	public String getMessage() {
		return message;
	}
	public Integer getCode() {
		return code;
	}
	public void setData(T data) {
		this.data = data;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
}
