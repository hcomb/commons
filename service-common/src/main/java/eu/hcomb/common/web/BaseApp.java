package eu.hcomb.common.web;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;
import io.swagger.jaxrs.config.BeanConfig;

import java.security.Key;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.client.Client;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import eu.hcomb.common.auth.JWTAuthFilter;
import eu.hcomb.common.auth.TokenAuthenticator;
import eu.hcomb.common.auth.UserAuthorizer;
import eu.hcomb.common.cors.CorsConfigurable;
import eu.hcomb.common.dto.User;
import eu.hcomb.common.service.EventEmitter;
import eu.hcomb.common.service.TokenService;
import eu.hcomb.common.service.impl.TokenServiceImpl;
import eu.hcomb.common.swagger.SwaggerConfigurable;

public abstract class BaseApp<T extends BaseConfig> extends Application<T> implements Module {

	public static final String DEFAULT_AUTH_BEARER = "Bearer";
	public static final String DEFAULT_AUTH_REALM = "realm";
	public static final String DEFAULT_AUTH_COOKIE = "jwt";
	public static final String DEFAULT_AUTH_URL_PARAM = "token";

	protected Log log = LogFactory.getLog(this.getClass());
	
	protected Injector injector;
	
	protected BaseConfig configuration;
	protected Environment environment;
	
	protected Client client;
	
	@Override
	public void initialize(Bootstrap<T> bootstrap) {
		bootstrap.getObjectMapper().setSerializationInclusion(Include.NON_NULL);


	}
	
	protected void init(Environment environment, T configuration){
    	this.configuration =  configuration;
    	this.environment = environment;
    	
		client = new JerseyClientBuilder(environment).using(this.configuration.getJerseyClientConfiguration()).build(getName());
	}
	
    protected void defaultConfig(Environment environment, T configuration) {
    	
    	removeDropwizardExceptionMappers(environment);
    	
        setupSecurity(environment);

		enableCors(environment, configuration);

		EventEmitter eventEmitter = injector.getInstance(EventEmitter.class);
		if(!"rrouter-service".equals(getName()))
			eventEmitter.init(getName(), environment);
		
    }
        
	public void configureSecurity(Binder binder) {
		
		binder
			.bind(TokenService.class)
			.to(TokenServiceImpl.class);
		
		binder
			.bind(Authenticator.class)
			.to(TokenAuthenticator.class);

		binder
			.bind(Authorizer.class)
			.to(UserAuthorizer.class);
		
	}
    
	protected void setUpSwagger(BaseConfig config, Environment environment) {
		if(config instanceof SwaggerConfigurable){
			log.info("configuring swagger");
			SwaggerConfigurable swagger = (SwaggerConfigurable)config;
			
			DefaultServerFactory defaultServerFactory = (DefaultServerFactory) configuration.getServerFactory();
			String basePath = defaultServerFactory.getApplicationContextPath();
			
			environment.jersey().register(io.swagger.jaxrs.listing.ApiListingResource.class);
			environment.jersey().register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

			BeanConfig beanConfig = new BeanConfig();
	        beanConfig.setHost(swagger.getSwaggerConfig().getBaseUrl());
	        beanConfig.setBasePath(basePath);
	        beanConfig.setResourcePackage(swagger.getSwaggerConfig().getResourcePackages());
	        beanConfig.setScan(true);
	        
		}else{
			log.warn("cannot configure swagger");
		}
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
    
    protected void enableCors(Environment environment, BaseConfig config){
    	if(config instanceof CorsConfigurable){
    		log.info("enabling CORS filter");
    		CorsConfigurable cors = (CorsConfigurable)config;
            FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORSFilter", CrossOriginFilter.class);

            filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, environment.getApplicationContext().getContextPath() + "*");
            filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, cors.getCorsConfig().getMethods());
            filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, cors.getCorsConfig().getOrigins());
            filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, cors.getCorsConfig().getHeaders());
            filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, cors.getCorsConfig().getAllowCredentials());
    	}else{
    		log.warn("cannot enable CORS filter, config is not an instance of CorsConfigurable");
    	}
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
	
	@Provides
	@Named("rrouter.url")
	public String getRRouterUrl(){
		return configuration.getRrouterUrl();
	}

	@Provides
	public Client getClient(){
		return this.client;
	}
}
