package eu.hcomb.common.service;

import eu.hcomb.common.dto.Token;
import eu.hcomb.common.dto.User;


public interface TokenService {

	public abstract Token getToken(String subject, String[] roles);

	public abstract User getUser(String token);

}
