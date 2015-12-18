package eu.hcomb.common.service;

public interface EventEmitter {

	public void emit(String event, Object data);
	
}
