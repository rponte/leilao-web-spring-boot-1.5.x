package br.com.mdias.leilaoweb.controller;

import java.security.Principal;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mdias.leilaoweb.controller.response.jsend.Json;
import br.com.mdias.leilaoweb.controller.response.jsend.JsonResult;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class IndexController {

	@GetMapping("/index")
	public JsonResult<String> welcome(Principal principal) {
		
		String username = "anonymous";
		if (principal != null) {
			username = principal.getName();
		}
		
		String welcome = String.format("Welcome to app, %s", username);
		return Json.success()
				   .withData(welcome)
				   .build();
	}
	
}
