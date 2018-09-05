package br.com.mdias.leilaoweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

/**
 * <p>Create a Deployable War File
 * <br>https://docs.spring.io/spring-boot/docs/current/reference/html/howto-traditional-deployment.html#howto-create-a-deployable-war-file
 * 
 * <p>Deploying a WAR to WebLogic
 * <br>https://docs.spring.io/spring-boot/docs/current/reference/html/howto-traditional-deployment.html#howto-weblogic
 */
@SpringBootApplication
public class LeilaoWebApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(LeilaoWebApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LeilaoWebApplication.class, args);
	}
}
