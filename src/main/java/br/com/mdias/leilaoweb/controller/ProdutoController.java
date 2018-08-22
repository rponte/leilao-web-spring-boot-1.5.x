package br.com.mdias.leilaoweb.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mdias.leilaoweb.model.Produto;

@RestController
@RequestMapping(value = "/produtos", produces = "application/json; charset=utf-8")
public class ProdutoController {
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscaPorId(@PathVariable("id") Integer id) {
		Produto ipad = new Produto(id, "iPad Retina Display", 4560.99);
		return ResponseEntity.ok(ipad);
	}

	@PostMapping("/salvar")
	public ResponseEntity<?> salvar(@Valid @RequestBody Produto produto) {
		
		if (produto.getId() < 0)
			throw new IllegalArgumentException("Id do produto inválido");
		
		ResponseEntity<Object> response = new ResponseEntity<Object>(HttpStatus.OK);
		return response;
	}
	
}
