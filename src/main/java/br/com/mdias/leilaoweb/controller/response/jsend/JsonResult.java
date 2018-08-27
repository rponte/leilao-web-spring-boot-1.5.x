package br.com.mdias.leilaoweb.controller.response.jsend;

/**
 * Based on jSend JSON Response - http://labs.omniti.com/labs/jsend
 */
public class JsonResult {

	private Status status;
	private Object data;
	
	private String message;
	private Integer code;
	
	public Object getData() {
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
	public void setData(Object data) {
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
