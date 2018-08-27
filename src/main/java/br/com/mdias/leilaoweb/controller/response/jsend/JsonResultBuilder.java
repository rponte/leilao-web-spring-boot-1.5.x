package br.com.mdias.leilaoweb.controller.response.jsend;

/**
 * Builder para criar instância de <code>JsonResult</code>. Recomenda-se
 * favorecer o uso da classe <code>Json</code>.
 */
public class JsonResultBuilder {
	
	private JsonResult jsonResult;
	
	public JsonResultBuilder success() {
		this.jsonResult = new JsonResult();
		this.jsonResult.setStatus(Status.SUCCESS);
		return this;
	}

	public JsonResultBuilder error() {
		this.jsonResult = new JsonResult();
		this.jsonResult.setStatus(Status.ERROR);
		return this;
	}
	
	public JsonResultBuilder fail() {
		this.jsonResult = new JsonResult();
		this.jsonResult.setStatus(Status.FAIL);
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
