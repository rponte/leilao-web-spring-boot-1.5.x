package br.com.mdias.leilaoweb.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import br.com.mdias.leilaoweb.config.SecurityConfig;

@RunWith(SpringRunner.class)
@WebMvcTest(value=MyApiController.class, secure=true)
@Import({ SecurityConfig.class })
public class MyApiControllerTest {
	
	@Autowired
	protected MockMvc mvc;

	@Test
	public void deveAcessarRecursoPublico_comOuSemCredenciaisInformadas() throws Exception {
		
		// COM credencias
		String username = "rponte";
		String password = "pia-limpa";
		mvc.perform(get("/api/public/say-my-name")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.with(httpBasic(username, password)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").value(username))
			;
		
		// SEM credencias
		mvc.perform(get("/api/public/say-my-name")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data").value("anonymous"))
		;
		
	}
	
	@Test
	public void deveAcessarRecursoProtegido_quandoCredenciaisInformadas() throws Exception {
		
		// COM credencias - ROLE_USER
		String username = "rponte";
		String password = "pia-limpa";
		
		mvc.perform(get("/api/protected/say-my-age")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.with(httpBasic(username, password)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(username.length()))
			;
		
		// COM credencias - ROLE_ADMIN
		username = "admin";
		password = "admin";
		
		mvc.perform(get("/api/protected/say-my-age")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.with(httpBasic(username, password)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(username.length()))
			;
		
	}
	
	@Test
	public void naoDeveAcessarRecursoProtegido_quandoCredenciaisNaoInformadas() throws Exception {
		// ação e validação
		mvc.perform(get("/api/protected/say-my-age")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED))
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"))
			;
	}
	
	@Test
	public void deveAcessarRecursoPrivado_quandoCredenciaisInformadas() throws Exception {
		
		// COM credencias - ROLE_ADMIN
		String username = "admin";
		String password = "admin";
		
		String expectedUser = System.getProperty("user.name");
		
		mvc.perform(get("/api/private/machine-username")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.with(httpBasic(username, password)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").value(expectedUser))
			;
		
	}
	
	@Test
	public void naoDeveAcessarRecursoPrivado_quandoUsuarioNaoTiverPermissao() throws Exception {
		
		// COM credencias mas sem permissao
		String username = "rponte";
		String password = "pia-limpa";
		
		mvc.perform(get("/api/private/machine-username")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.with(httpBasic(username, password)))
			.andDo(print())
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.message").value("Access is denied"))
			;
		
	}
	
	@Test
	public void naoDeveAcessarRecursoPrivado_quandoCredenciaisInvalidas() throws Exception {
		
		// COM credencias invalidas
		String username = "admin";
		String password = "senha-errada";
		
		mvc.perform(get("/api/private/machine-username")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.with(httpBasic(username, password)))
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.message").value("Bad credentials"))
			;
		
	}
	
	@Test
	public void deveAcessarRecursoPrivado_quandoCredenciaisInformadas_viaSSO() throws Exception {
		
		// COM credencias - ROLE_ADMIN
		String username = "admin";
		
		String expectedUser = System.getProperty("user.name");
		
		mvc.perform(get("/api/private/machine-username")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.with(withSsoUser(username))
					)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("data").value(expectedUser))
			;
		
	}

	private RequestPostProcessor withSsoUser(final String username) {
		return new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				Principal userPrincipal = new PreAuthenticatedAuthenticationToken(username, "N/A");
				request.setUserPrincipal(userPrincipal);
				return request;
			}
		};
	}

}
