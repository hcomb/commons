package eu.hcomb.common.web;

import io.dropwizard.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseConfig extends Configuration {

	protected Log log = LogFactory.getLog(this.getClass());

	private String authKey;
	private Long authTimeout;

	public Long getAuthTimeout() {
		return authTimeout;
	}

	public String getAuthKey() {
		return authKey;
	}
	
}
