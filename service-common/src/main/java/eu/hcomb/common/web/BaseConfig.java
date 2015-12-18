package eu.hcomb.common.web;

import io.dropwizard.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseConfig extends Configuration {

	protected Log log = LogFactory.getLog(this.getClass());

	protected String authKey;
	
	protected Long authTimeout;

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
	
}
