package eu.hcomb.common.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import eu.hcomb.common.dto.Token;
import eu.hcomb.common.dto.User;
import eu.hcomb.common.service.BaseService;
import eu.hcomb.common.service.TokenService;

@Singleton
public class TokenServiceImpl extends BaseService implements TokenService {

	private static final String ROLES_KEY = "roles";

	@Inject
	@Named("authKey")
	protected Key key;

	@Inject
	@Named("authTimeout")
	protected Long timeout;
	
	public Token getToken(String subject, String[] roles){
		
		Long expire = System.currentTimeMillis() + (timeout * 1000);
		
		String token = Jwts
				.builder()
				.setSubject(subject)
				.setExpiration(new Date(expire))
				.claim(ROLES_KEY, roles)
				.signWith(SignatureAlgorithm.HS512, key)
				.compact();

		Token ret = new Token();
		ret.setValid(true);
		ret.setValue(token);
		ret.setExpire(expire);
		
		return ret;
		
	}
	
	@SuppressWarnings("unchecked")
	public User getUser(String token){
		Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
		
		User user = new User();
		user.setName(jws.getBody().getSubject());
		user.setRoles(jws.getBody().get(ROLES_KEY, ArrayList.class));
		
		return user;
	}

}
