package eu.hcomb.common.auth;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthenticationException;
import io.jsonwebtoken.SignatureException;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Optional;

import eu.hcomb.common.dto.User;

//from https://github.com/ToastShaman/dropwizard-auth-jwt

@Priority(Priorities.AUTHENTICATION)
public class JWTAuthFilter<P extends User> extends AuthFilter<String, P> {

	protected Log log = LogFactory.getLog(this.getClass());
	
	private final String cookieName;
	private final String paramName;
	
	public JWTAuthFilter(String paramName, String cookieName) {
		this.paramName = paramName;
		this.cookieName = cookieName;
	}
	
	public void filter(final ContainerRequestContext requestContext) throws IOException {

		final Optional<String> optionalToken = getTokenFromRequest(requestContext);
		
		if (optionalToken.isPresent()) {
            try {
                final String token = optionalToken.get();
                final Optional<P> principal = authenticator.authenticate(token);

                if (principal.isPresent()) {
                    requestContext.setSecurityContext(new SecurityContext() {

                        public Principal getUserPrincipal() {
                            return principal.get();
                        }

                        public boolean isUserInRole(String role) {
                            return authorizer.authorize(principal.get(), role);
                        }

                        public boolean isSecure() {
                            return requestContext.getSecurityContext().isSecure();
                        }

                        public String getAuthenticationScheme() {
                            return SecurityContext.BASIC_AUTH;
                        }

                    });
                    
                    return;
                }
            } catch (SignatureException ex) {
                log.warn("Invalid signature: " + ex.getMessage(), ex);
            } catch (AuthenticationException ex) {
                log.warn("Error authenticating credentials", ex);
                throw new InternalServerErrorException();
            }
        }

        throw new WebApplicationException(unauthorizedHandler.buildResponse(prefix, realm));
	}

	public Optional<String> getTokenFromRequest(ContainerRequestContext requestContext) {
        final Optional<String> headerToken = TokenUtils.getTokenFromHeader(requestContext.getHeaders(), prefix);

        if (headerToken.isPresent()) {
            return headerToken;
        }

        final Optional<String> cookieToken = TokenUtils.getTokenFromCookie(requestContext.getCookies(), cookieName);
        
        if(cookieToken.isPresent()){
        	return cookieToken;
        }

        final Optional<String> paramToken = TokenUtils.getTokenFromParam(requestContext.getUriInfo().getQueryParameters(), paramName);
        
        return paramToken.isPresent() ? paramToken : Optional.<String>absent();
    }

    public static class Builder<P extends User> extends AuthFilterBuilder<String, P, JWTAuthFilter<P>> {

        private String cookieName;
        private String paramName;

        public Builder<P> setCookieName(String cookieName) {
            this.cookieName = cookieName;
            return this;
        }

        public Builder<P> setParamName(String paramName) {
            this.paramName = paramName;
            return this;
        }

        @Override
        protected JWTAuthFilter<P> newInstance() {
            return new JWTAuthFilter(paramName, cookieName);
        }
    }
}
