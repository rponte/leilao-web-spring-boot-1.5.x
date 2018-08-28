package br.com.mdias.leilaoweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ObjectMapper jsonMapper;
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    		.and()
        	.authorizeRequests()
        		.antMatchers(HttpMethod.GET, "/produtos/xxx/private-area").hasAnyAuthority("ROLE_XXX")
            .anyRequest()
            	.authenticated()
            .and().csrf().disable()
            .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler())
            .authenticationEntryPoint(customAuthenticationEntryPoint())
            //.disable().anonymous()
            ;
    }
    
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler(jsonMapper);
    }
    
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
    	return new CustomAuthenticationEntryPoint(jsonMapper);
    }
}
