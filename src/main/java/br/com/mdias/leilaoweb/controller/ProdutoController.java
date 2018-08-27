package br.com.mdias.leilaoweb.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mdias.leilaoweb.controller.response.errors.validation.Errors;
import br.com.mdias.leilaoweb.controller.response.jsend.Json;
import br.com.mdias.leilaoweb.controller.response.jsend.JsonResult;
import br.com.mdias.leilaoweb.model.Produto;

@RestController
@RequestMapping(value = "/produtos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProdutoController {
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/{id}")
	public JsonResult buscaPorId(@PathVariable("id") Integer id) {
		
		Produto ipad = new Produto(id, "iPad Retina Display", 4560.99);
		
		return Json.success()
				   .withData(ipad)
				   .build();
	}

	@PostMapping("/salvar")
	public JsonResult salvar(@Valid @RequestBody Produto produto, BindingResult result) {
		
		// erro de validação
		if (result.hasErrors()) {
			
			Errors errors = new Errors();
			
			List<FieldError> fieldErrors = result.getFieldErrors();
			for (FieldError fieldError : fieldErrors) {
				String field = fieldError.getField();
				String message = messageSource.getMessage(fieldError, new Locale("pt", "BR"));
				errors
					.with(field, message);
					;
			}
			
			return Json.fail()
					   .withMessage("Erro de validação")
					   .withData(errors)
					   .build();
		}
		
		// logica de negocio com mensagem
		if (existe(produto)) {
			return Json.error()
					   .withMessage("Produto já existente no sistema")
					   .build();
		}
		
		// logica de negocio com mensagem e payload
		if (substiuido(produto)) {
			return Json.error()
					   .withMessage("Produto substituido por nova edição", 7001)
					   .withData(new Produto(-70, produto.getNome(), 999.80))
					   .build();
		}
		
		// sucesso
		return Json.success()
				   .withData(produto)
				   .withMessage("Produto salvo com sucesso!")
				   .build();
	}

	private boolean substiuido(Produto produto) {
		return produto.getId() == -69;
	}

	private boolean existe(Produto produto) {
		return produto.getId() == -24;
	}
	
}
