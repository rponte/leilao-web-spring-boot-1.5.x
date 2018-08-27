package br.com.mdias.leilaoweb.controller.response.jsend;

public enum Status {

	/**
	 * All went well, and (usually) some data was returned
	 */
	SUCCESS(200),
	/**
	 * There was a problem with the data submitted, or some pre-condition of the API call wasn't satisfied
	 */
	FAIL(400),
	/**
	 * An error occurred in processing the request, i.e. an exception was thrown
	 */
	ERROR(500);
	
	private int defaultHttpCode;
	
	private Status(int defaultHttpCode) {
		this.defaultHttpCode = defaultHttpCode;
	}
	
	public int getHttpStatusCode() {
		return defaultHttpCode;
	}
	
}
