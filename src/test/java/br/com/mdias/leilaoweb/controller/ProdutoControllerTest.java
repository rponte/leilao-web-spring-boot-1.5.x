package br.com.mdias.leilaoweb.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.mvc.PayloadExtractor;
import br.com.mdias.leilaoweb.config.SecurityConfig;
import br.com.mdias.leilaoweb.controller.response.errors.validation.Errors;
import br.com.mdias.leilaoweb.controller.response.jsend.Status;
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
@WithMockUser("rponte")
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
	
	private PayloadExtractor payloadExtractor;
	
	@Before
	public void init() {
		this.payloadExtractor = new PayloadExtractor(jsonMapper);
	}
	
	@Test
	public void deveRetornarRespostaDeSucesso() throws Exception {
		
		// ação e validação
		mvc.perform(get("/produtos/2020")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("status").value(Status.SUCCESS.name()))
			.andExpect(jsonPath("data").isNotEmpty())
			.andExpect(jsonPath("message").isEmpty())
			.andExpect(jsonPath("code").isEmpty())
			;
	}
	
	@Test
	public void deveRetornarRespostaDeSucesso_comMensagemDeSucesso() throws Exception {
		
		// cenário
		Produto iphone = new Produto(2020, "iPhone X", 5989.90);
		
		// ação e validação
		mvc.perform(post("/produtos/salvar")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(iphone)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("status").value(Status.SUCCESS.name()))
			.andExpect(jsonPath("data").exists())
			.andExpect(jsonPath("message").value("Produto salvo com sucesso!"))
			.andExpect(jsonPath("code").isEmpty())
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
			.andExpect(jsonPath("status").value(Status.FAIL.name()))
			.andExpect(jsonPath("data").isNotEmpty())
			.andExpect(jsonPath("message").value("Erro de validação"))
			.andExpect(jsonPath("code").isEmpty())
			.andDo(payloadExtractor)
			.andReturn()
			;
		
		Errors errors = payloadExtractor.as(Errors.class);
		assertEquals(3, errors.getErrors().size());
	}

	@Test
	public void deveRetornarRespostaDeErro_paraErroDeLogicaDeNegocio() throws Exception {
		
		// cenário
		Produto invalido = new Produto(-24, "Geladeira", 780.0);
		
		// ação e validação
		mvc.perform(post("/produtos/salvar")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(invalido)))
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("status").value(Status.ERROR.name()))
			.andExpect(jsonPath("data").isEmpty())
			.andExpect(jsonPath("message").value("Produto já existente no sistema"))
			.andExpect(jsonPath("code").isEmpty())
			;
	}
	
	@Test
	public void deveRetornarRespostaDeErroComInfosAdicionais_paraErroDeLogicaDeNegocio() throws Exception {
		
		// cenário
		Produto invalido = new Produto(-69, "Fogão", 780.0);
		
		// ação e validação
		mvc.perform(post("/produtos/salvar")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(invalido)))
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("status").value(Status.ERROR.name()))
			.andExpect(jsonPath("data").isNotEmpty())
			.andExpect(jsonPath("message").value("Produto substituido por nova edição"))
			.andExpect(jsonPath("code").value(7001))
			;
	}
	
	/**
	 * ****
	 * Testes para tratamentos customizados e automaticos do Spring MVC
	 * **** 
	 */
	
	@Test
	public void deveRetornarRespostaDeErro_paraJsonComFormatoInvalido_automatico() throws Exception {
		
		// cenário
		Produto invalido = new Produto(null, "", 0.0);
		String jsonComTipagemInvalida = toJson(invalido).replace("0.0", "\"not_a_number\"");
		
		// ação e validação
		String expectedError = "JSON parse error: Can not deserialize value of type java.lang.Double from String \"not_a_number\": not a valid Double value";
		
		mvc.perform(post("/produtos/salvar")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonComTipagemInvalida))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("status").value(Status.FAIL.name()))
			.andExpect(jsonPath("data").isEmpty())
			.andExpect(jsonPath("message").value(containsString(expectedError)))
			.andExpect(jsonPath("code").isEmpty())
			;
	}
	
	@Test
	public void deveRetornarRespostaDeErro_paraCamposInvalidos_automatico() throws Exception {
		
		// cenário
		Produto invalido = new Produto(null, "", 0.0);
		
		// ação e validação
		mvc.perform(post("/produtos/salvar-smart")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(invalido)))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("status").value(Status.FAIL.name()))
			.andExpect(jsonPath("data").isNotEmpty())
			.andExpect(jsonPath("message").value("Erro de validação"))
			.andExpect(jsonPath("code").isEmpty())
			.andDo(payloadExtractor)
			.andReturn()
			;
		
		Errors errors = payloadExtractor.as(Errors.class);
		assertEquals(3, errors.getErrors().size());
	}
	
	@Test
	public void deveRetornarRespostaDeErro_paraErroDeLogicaDeNegocio_automatica() throws Exception {
		
		// cenário
		Produto invalido = new Produto(-24, "Geladeira", 780.0);
		
		// ação e validação
		mvc.perform(post("/produtos/salvar-smart")
					.contentType(MediaType.APPLICATION_JSON)
					.content(toJson(invalido)))
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("status").value(Status.ERROR.name()))
			.andExpect(jsonPath("data").isEmpty())
			.andExpect(jsonPath("message").value("Produto já existente no sistema"))
			.andExpect(jsonPath("code").isEmpty())
			;
	}
	
	private String toJson(Produto produto) throws JsonProcessingException {
		return jsonMapper.writeValueAsString(produto);
	}

}
