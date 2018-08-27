package br.com.mdias.leilaoweb.controller.response.jsend;

public class JsonResultBuilder {
	
	private JsonResult jsonResult;
	
	public JsonResultBuilder success() {
		this.jsonResult = new JsonResult();
		this.jsonResult.setStatus(JsonResult.Status.SUCCESS);
		return this;
	}

	public JsonResultBuilder error() {
		this.jsonResult = new JsonResult();
		this.jsonResult.setStatus(JsonResult.Status.ERROR);
		return this;
	}
	
	public JsonResultBuilder fail() {
		this.jsonResult = new JsonResult();
		this.jsonResult.setStatus(JsonResult.Status.FAIL);
		return this;
	}

	public JsonResultBuilder withMessage(String message) {
		this.jsonResult.setMessage(message);
		return this;
	}
	
	public JsonResultBuilder withMessage(String message, int errorCode) {
		this.jsonResult.setMessage(message);
		this.withCode(errorCode);
		return this;
	}

	public JsonResultBuilder withData(Object data) {
		this.jsonResult.setData(data);
		return this;
	}
	
	public JsonResultBuilder withCode(int errorCode) {
		this.jsonResult.setCode(errorCode);
		return this;
	}
	
	public JsonResult build() {
		return jsonResult;
	}

}
