package br.com.mdias.leilaoweb.controller.response.jsend;

/**
 * Classe utilitária para simplificar a vida do desenvolvedor na hora de criar
 * uma instância de <code>JsonResult</code>
 */
public class Json {

	public static <T> JsonResultBuilder<T> success() {
		return new JsonResultBuilder<T>().success();
	}
	
	public static <T> JsonResultBuilder<T> error() {
		return new JsonResultBuilder<T>().error();
	}
	
	public static <T> JsonResultBuilder<T> fail() {
		return new JsonResultBuilder<T>().fail();
	}
}