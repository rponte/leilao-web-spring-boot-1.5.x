package br.com.mdias.leilaoweb.controller.response.jsend;

/**
 * Classe utilitária para simplificar a vida do desenvolvedor na hora de criar
 * uma instância de <code>JsonResult</code>
 */
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