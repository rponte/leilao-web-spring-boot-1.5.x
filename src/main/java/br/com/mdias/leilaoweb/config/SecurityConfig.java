package br.com.mdias.leilaoweb.config;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mdias.leilaoweb.config.security.CustomAccessDeniedHandler;
import br.com.mdias.leilaoweb.config.security.CustomAuthenticationEntryPoint;
import br.com.mdias.leilaoweb.config.security.CustomAuthenticationSuccessHandler;
import br.com.mdias.leilaoweb.config.security.CustomBasicAuthenticationEntryPoint;

/**
 * https://www.baeldung.com/securing-a-restful-web-service-with-spring-security
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ObjectMapper jsonMapper;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("rponte").password("pia-limpa").roles("USER").and()
			.withUser("admin").password("admin").roles("ADMIN", "USER").and()
			// mdias users
			.withUser("ter00942").password("ter00942").roles("ADMIN", "USER").and()
			.withUser("ter01205").password("ter01205").roles("USER")
			;
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web
        	.ignoring()
        	.antMatchers(OPTIONS, "/**");
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.requestCache().disable()
        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    		.and()
        	.authorizeRequests()
        		.antMatchers("/index").permitAll()
        		.antMatchers("/admin/**").hasRole("ADMIN")
        		.antMatchers("/login*").permitAll()
        		.antMatchers("/api/public/**").permitAll()
        		.antMatchers("/api/protected/**").hasAnyRole("USER", "ADMIN")
        		.antMatchers("/api/private/**").hasRole("ADMIN")
        		.antMatchers(GET, "/produtos/xxx/private-area").hasAnyAuthority("ROLE_XXX")
            .anyRequest()
            	.authenticated()
            .and()
            	.formLogin()
            		.successHandler(customAuthenticationSuccessHandler())
            .and()
            	.httpBasic()
            		.authenticationEntryPoint(customBasicAuthenticationEntryPoint())
            .and()
            	.jee()
            		.authenticatedUserDetailsService(customAuthenticatedUserDetailsService())
            .and()
            	.csrf().disable()
	            .exceptionHandling()
	            	.accessDeniedHandler(customAccessDeniedHandler())
	            	.authenticationEntryPoint(customAuthenticationEntryPoint())
//	            .disable().anonymous()
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
    
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
    	return new CustomAuthenticationSuccessHandler();
    }
    
    @Bean
    public AuthenticationEntryPoint customBasicAuthenticationEntryPoint() {
    	CustomBasicAuthenticationEntryPoint basicHttp = new CustomBasicAuthenticationEntryPoint(jsonMapper);
    	basicHttp.setRealmName("MDIAS");
		return basicHttp;
    }
    
    @Bean
    public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> customAuthenticatedUserDetailsService() throws Exception {
    	UserDetailsService userDetailsService = super.userDetailsServiceBean();
		UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>(userDetailsService);
		return wrapper;
	}
    
}
