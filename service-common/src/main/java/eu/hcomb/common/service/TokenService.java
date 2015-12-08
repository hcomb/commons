package eu.hcomb.common.service;

import java.util.List;

import eu.hcomb.common.dto.Token;
import eu.hcomb.common.dto.User;


public interface TokenService {

	public abstract Token getToken(String subject, List<String> roles);

	public abstract User getUser(String token);

}
