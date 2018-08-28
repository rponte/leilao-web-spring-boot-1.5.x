package br.com.mdias.leilaoweb.controller;

import java.util.Arrays;
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
import br.com.mdias.leilaoweb.model.ProdutoInvalidoException;

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
	
	@GetMapping("/lista-todos")
	public JsonResult lista() {
		
		Produto ipad = new Produto(991, "iPad Retina Display", 4560.99);
		Produto iphone = new Produto(992, "iPhone 8 Plus", 4400.91);
		
		List<Produto> produtos = Arrays.asList(ipad, iphone);
		
		return Json.success()
				   .withData(produtos)
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
	
	@PostMapping("/salvar-smart")
	public JsonResult salvar(@Valid @RequestBody Produto produto) {

		// logica de negocio com mensagem
		if (existe(produto)) {
			throw new ProdutoInvalidoException("Produto já existente no sistema");
		}
		
		// sucesso
		return Json.success()
				   .withData(produto)
				   .withMessage("Produto salvo com sucesso!")
				   .build();
	}
	
	@GetMapping("/xxx/private-area")
	public JsonResult privateArea() {
		
		Produto video = new Produto(69, "Porno video", 599.91);
		
		return Json.success()
				   .withData(video)
				   .build();
	}

	private boolean substiuido(Produto produto) {
		return produto.getId() == -69;
	}

	private boolean existe(Produto produto) {
		return produto.getId() == -24;
	}
	
}
