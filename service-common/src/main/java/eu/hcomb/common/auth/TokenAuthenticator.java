package eu.hcomb.common.auth;

import io.dropwizard.auth.Authenticator;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.hcomb.common.dto.User;
import eu.hcomb.common.service.TokenService;

@Singleton
public class TokenAuthenticator implements Authenticator<String, User> {
	
	@Inject
	protected TokenService tokenService;
	
    public Optional<User> authenticate(String token) {

    	User user = tokenService.getUser(token);

    	return Optional.of(user);
    }

}
