package br.com.mdias.leilaoweb.controller;

import java.security.Principal;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mdias.leilaoweb.controller.response.jsend.Json;
import br.com.mdias.leilaoweb.controller.response.jsend.JsonResult;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MyApiController {

	@GetMapping("/public/say-my-name")
	public JsonResult<String> sayMyName(Principal principal) {
		
		String username = "anonymous";
		if (principal != null) {
			username = principal.getName();
		}
		
		return Json.success()
				   .withData(username)
				   .build();
	}
	
	@GetMapping("/protected/say-my-age")
	public JsonResult<String> sayMyAge(Principal principal) {
		
		String username = principal.getName();
		return Json.success()
				   .withData(username.length())
				   .build();
	}
	
	@GetMapping("/private/machine-username")
	public JsonResult<String> machineUsername() {
		
		String username = System.getProperty("user.name");
		return Json.success()
				   .withData(username)
				   .build();
	}
}
