package pl.com.januszex.paka.security;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class CookieAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    public static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_START = "Bearer ";


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return "N/A";
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_START)) {
            return null;
        }
        return authHeader.substring(BEARER_START.length());
    }

}
