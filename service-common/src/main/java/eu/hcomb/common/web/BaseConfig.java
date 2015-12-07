package eu.hcomb.common.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.dropwizard.Configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseConfig extends Configuration {

	protected Log log = LogFactory.getLog(this.getClass());

	@JsonProperty
	private String authKey;

	public String getAuthKey() {
		return authKey;
	}

	@JsonProperty
	private Long authTimeout;

	public Long getAuthTimeout() {
		return authTimeout;
	}


}
