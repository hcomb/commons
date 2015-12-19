package eu.hcomb.common.web;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseConfig extends Configuration {

	protected Log log = LogFactory.getLog(this.getClass());

	protected String authKey;
	
	protected Long authTimeout;
	
    protected String rrouterUrl;
    
    @JsonProperty("jerseyClient")
    protected JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

	public Long getAuthTimeout() {
		return authTimeout;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public void setAuthTimeout(Long authTimeout) {
		this.authTimeout = authTimeout;
	}

	public String getRrouterUrl() {
		return rrouterUrl;
	}

	public void setRrouterUrl(String rrouterUrl) {
		this.rrouterUrl = rrouterUrl;
	}

	public JerseyClientConfiguration getJerseyClient() {
		return jerseyClient;
	}

	public void setJerseyClient(JerseyClientConfiguration jerseyClient) {
		this.jerseyClient = jerseyClient;
	}
	
}
