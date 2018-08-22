package br.com.mdias.leilaoweb.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mdias.leilaoweb.config.SecurityConfig;
import br.com.mdias.leilaoweb.model.Produto;

/**
 * Handling Standard Spring MVC Exceptions
 * https://docs.spring.io/spring/docs/3.2.x/spring-framework-reference/html/mvc.html#mvc-ann-rest-spring-mvc-exceptions
 * 
 * https://www.baeldung.com/exception-handling-for-rest-with-spring
 * https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value=ProdutoController.class, secure=true)
@Import(SecurityConfig.class)
//@WithMockUser("rponte")

/**
 * Teste levantando todo o contexto do Spring (moda antiga)
 */
//@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
public class ProdutoControllerTest {
	
	@Autowired
	protected MockMvc mvc;
	
	@Autowired
	private ObjectMapper jsonMapper;
	
	@Test
	public void deveRetornarRespostaDeSucesso() throws Exception {
		
		// ação e validação
		mvc.perform(get("/produtos/2020")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED))
			.andDo(print())
			.andExpect(status().isOk())
			// TODO: validar json de resposta
			;
	}
	
	@Test
	public void deveRetornarRespostaDeSucesso_paraCamposValidos() throws Exception {
		
		// cenário
		Produto iphone = new Produto(2020, "iPhone X", 5989.90);
		
		// ação e validação
		mvc.perform(post("/produtos/salvar")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(iphone)))
			.andDo(print())
			.andExpect(status().isOk())
			// TODO: validar json de resposta
			;
	}

	@Test
	public void deveRetornarRespostaDeErro_paraCamposInvalidos() throws Exception {
		
		// cenário
		Produto invalido = new Produto(null, "", 0.0);
		
		// ação e validação
		mvc.perform(post("/produtos/salvar")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(invalido)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			// TODO: validar json de resposta
			;
	}
	
	@Test
	public void deveRetornarRespostaDeErro_paraExcecaoDeLogicaDeNegocio() throws Exception {
		
		// cenário
		Produto invalido = new Produto(-1, "Geladeira", 780.0);
		
		// ação e validação
		mvc.perform(post("/produtos/salvar")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(invalido)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			// TODO: validar json de resposta
			;
	}
	
	private String toJson(Produto produto) throws JsonProcessingException {
		return jsonMapper.writeValueAsString(produto);
	}

}
