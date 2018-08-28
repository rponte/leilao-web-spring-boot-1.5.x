package br.com.mdias.leilaoweb.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.mdias.leilaoweb.model.Produto;

@Service
public class ProdutoService {

	private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);
	
	@Autowired
    private Validator validator;
	
	public void atualiza(Produto produto) {
		
		logger.info("Updating entity Produto with: {}", produto);
		
		Set<ConstraintViolation<Produto>> violations = validator.validate(produto);
		for (ConstraintViolation<Produto> violation : violations) {
			logger.error("field error '{}': '{}'", violation.getPropertyPath(), violation.getMessage()); 
		}
		
		throw new ConstraintViolationException(violations);
	}
	
}
