package eu.hcomb.common.service;

import javax.ws.rs.core.MultivaluedMap;

import io.dropwizard.setup.Environment;

public interface EventEmitter {

	public void emit(String event, Object data);

	public void emit(String string, Object data, String method,
			String url, MultivaluedMap<String, String> queryParameters,
			MultivaluedMap<String, String> requestHeaders, String remoteAddress);

	public abstract void init(String serviceName, Environment environment);

	
}
