package br.com.mdias.leilaoweb.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mdias.leilaoweb.controller.response.jsend.Json;
import br.com.mdias.leilaoweb.controller.response.jsend.JsonResult;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MyApiController {
	
	@GetMapping("/public/who-am-i")
	public JsonResult<?> whoAmI(@AuthenticationPrincipal UserDetails user) {
		
		if (user == null) {
			return Json.fail()
					   .withData("Sorry Mr Anonymous, we have no idea who you are!")
					   .build();
		}
		
		return Json.success()
				   .withData(user)
				   .build();
	}

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
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/protected/what-time-is-it")
	public JsonResult<String> whatTimeIsIt() {
		String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
		return Json.success()
				   .withData(now)
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
