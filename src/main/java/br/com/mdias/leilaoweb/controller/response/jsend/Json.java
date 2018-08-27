package br.com.mdias.leilaoweb.controller.response.jsend;

public class Json {

	public static JsonResultBuilder success() {
		return new JsonResultBuilder().success();
	}
	
	public static JsonResultBuilder error() {
		return new JsonResultBuilder().error();
	}
	
	public static JsonResultBuilder fail() {
		return new JsonResultBuilder().fail();
	}
}