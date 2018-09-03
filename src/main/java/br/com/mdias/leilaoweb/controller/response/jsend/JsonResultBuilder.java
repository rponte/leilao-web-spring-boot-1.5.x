package br.com.mdias.leilaoweb.controller.response.jsend;

/**
 * Builder para criar instância de <code>JsonResult</code>. Recomenda-se
 * favorecer o uso da classe <code>Json</code>.
 */
public class JsonResultBuilder<T> {
	
	private JsonResult<T> jsonResult;
	
	public JsonResultBuilder<T> success() {
		this.jsonResult = new JsonResult<T>();
		this.jsonResult.setStatus(Status.SUCCESS);
		return this;
	}

	public JsonResultBuilder<T> error() {
		this.jsonResult = new JsonResult<T>();
		this.jsonResult.setStatus(Status.ERROR);
		return this;
	}
	
	public JsonResultBuilder<T> fail() {
		this.jsonResult = new JsonResult<T>();
		this.jsonResult.setStatus(Status.FAIL);
		return this;
	}

	public JsonResultBuilder<T> withMessage(String message) {
		this.jsonResult.setMessage(message);
		return this;
	}
	
	public JsonResultBuilder<T> withMessage(String message, int errorCode) {
		this.jsonResult.setMessage(message);
		this.withCode(errorCode);
		return this;
	}

	public JsonResultBuilder<T> withData(T data) {
		this.jsonResult.setData(data);
		return this;
	}
	
	public JsonResultBuilder<T> withCode(int errorCode) {
		this.jsonResult.setCode(errorCode);
		return this;
	}
	
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> JsonResult<T> build() {
		return (JsonResult<T>) jsonResult;
	}
	
	public JsonResult<T> build2() {
		return jsonResult;
	}

}
