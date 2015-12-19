package eu.hcomb.common.service;

import io.dropwizard.setup.Environment;

public interface EventEmitter {

	public void emit(String event, Object data);

	public abstract void init(String serviceName, Environment environment);
	
}
