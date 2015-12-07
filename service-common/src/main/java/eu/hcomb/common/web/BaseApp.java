package eu.hcomb.common.web;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import eu.hcomb.common.auth.JWTAuthFilter;
import eu.hcomb.common.dto.User;

public abstract class BaseApp<T extends BaseConfig> extends Application<T> implements Module {

	public static final String DEFAULT_AUTH_BEARER = "Bearer";
	public static final String DEFAULT_AUTH_REALM = "realm";
	public static final String DEFAULT_AUTH_COOKIE = "jwt";
	public static final String DEFAULT_AUTH_URL_PARAM = "token";

	protected Log log = LogFactory.getLog(this.getClass());
	
	protected Injector injector;
	
	protected BaseConfig configuration;
	
	@Override
	public void initialize(Bootstrap<T> bootstrap) {
		bootstrap.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
	}
	
    protected void defaultConfig(Environment environment, T configuration) {
    	this.configuration =  configuration;
    	
    	removeDropwizardExceptionMappers(environment);
    }
    
    protected void setupSecurity(Environment environment){
    	setupSecurity(environment, DEFAULT_AUTH_URL_PARAM, DEFAULT_AUTH_COOKIE, DEFAULT_AUTH_REALM, DEFAULT_AUTH_BEARER);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setupSecurity(Environment environment, String paramName, String cookieName, String realm, String prefix){
        environment.jersey().register(new AuthDynamicFeature(
                new JWTAuthFilter.Builder()
                		.setParamName(paramName)
                		.setCookieName(cookieName)
                        .setRealm(realm)
                        .setPrefix(prefix)
                        .setAuthenticator(injector.getInstance(Authenticator.class))
                        .setAuthorizer(injector.getInstance(Authorizer.class))
                        .buildAuthFilter()
        			)
        		);
        environment.jersey().register(new AuthValueFactoryProvider.Binder(User.class));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
    }
    
	protected void removeDropwizardExceptionMappers(Environment environment) {
        ResourceConfig jrConfig = environment.jersey().getResourceConfig();
        Set<Object> dwSingletons = jrConfig.getSingletons();
        List<Object> singletonsToRemove = new ArrayList<Object>();

        for (Object s : dwSingletons) {
            if (s instanceof ExceptionMapper && s.getClass().getName().contains("dropwizard")) {
                singletonsToRemove.add(s);
            }
        }

        for (Object s : singletonsToRemove) {
            jrConfig.getSingletons().remove(s);
        }
	}
	

	@Provides
	@Named("authKey")
	public Key getSignKey(){
		byte[] bytes = new Base64Codec().decode(configuration.getAuthKey());
		Key key = new SecretKeySpec(bytes, SignatureAlgorithm.HS512.getJcaName());
		return key;
	}
	
	@Provides
	@Named("authTimeout")
	public Long getAuthTimeout(){
		return configuration.getAuthTimeout();
	}
}
