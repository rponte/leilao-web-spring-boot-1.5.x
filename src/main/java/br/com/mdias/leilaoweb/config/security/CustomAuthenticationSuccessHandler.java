package br.com.mdias.leilaoweb.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		// TODO: reimplemtar logica do onAuthenticationSuccess() aqui
		super.handle(request, response, authentication);
	}
	
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws ServletException, IOException {

		final SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest == null) {
			clearAuthenticationAttributes(request);
			return;
		}
		
		if (isAlwaysUseDefaultTargetUrl() || hasTargetUrlParameter(request)) {
			requestCache.removeRequest(request, response);
			clearAuthenticationAttributes(request);
			return;
		}

		clearAuthenticationAttributes(request);
	}
	
	private boolean hasTargetUrlParameter(HttpServletRequest request) {
		final String targetUrlParameter = getTargetUrlParameter();
		
		String targetUrl = request.getParameter(targetUrlParameter);
		return (targetUrlParameter != null && StringUtils.hasText(targetUrl));
	}

	public void setRequestCache(final RequestCache requestCache) {
		this.requestCache = requestCache;
	}
}
