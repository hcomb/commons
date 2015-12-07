package eu.hcomb.common.auth;

import io.dropwizard.auth.Authorizer;
import eu.hcomb.common.dto.User;

public class UserAuthorizer implements Authorizer<User> {

	public boolean authorize(User principal, String role) {
		if(principal.getRoles().contains(role))
			return true;
		else
			return false;
	};
	
}
