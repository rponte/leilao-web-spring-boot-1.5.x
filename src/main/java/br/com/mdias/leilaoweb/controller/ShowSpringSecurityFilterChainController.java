package br.com.mdias.leilaoweb.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mdias.leilaoweb.controller.response.jsend.Json;
import br.com.mdias.leilaoweb.controller.response.jsend.JsonResult;

/**
 * https://stackoverflow.com/questions/41480102/how-spring-security-filter-chain-works/41482134
 */
@RestController
@RequestMapping(value = "/api/public", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ShowSpringSecurityFilterChainController {

	@Lazy
	@Autowired
    private FilterChainProxy filterChainProxy;
	
	@GetMapping("/filter-chain")
	public JsonResult<?> filterChain() {
		
		Map<String, Map<Integer, String>> filters = getSecurityFilterChainProxy();
		
		return Json.success()
				   .withData(filters)
				   .build();
	}
	
	public Map<String, Map<Integer, String>> getSecurityFilterChainProxy() {
        Map<String, Map<Integer, String>> filterChains = new LinkedHashMap<>();
        int i = 1;
        for(SecurityFilterChain secfc : this.filterChainProxy.getFilterChains()){
        	Map<Integer, String> filters = new HashMap<Integer, String>();
            int j = 1;
            for(Filter filter : secfc.getFilters()){
                filters.put(j++, filter.getClass().getName());
            }
            RequestMatcher requestMatcher = ((DefaultSecurityFilterChain) secfc).getRequestMatcher();
			filterChains.put(i++ + ") " + requestMatcher, filters);
        }
        return filterChains;
    }
}
