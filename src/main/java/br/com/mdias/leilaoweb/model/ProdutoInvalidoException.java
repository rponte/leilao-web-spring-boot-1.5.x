package br.com.mdias.leilaoweb.model;

public class ProdutoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProdutoInvalidoException(String message) {
		super(message);
	}
	
}
