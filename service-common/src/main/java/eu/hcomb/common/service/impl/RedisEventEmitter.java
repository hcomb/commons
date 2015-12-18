package eu.hcomb.common.service.impl;

import com.google.inject.Singleton;

import eu.hcomb.common.service.EventEmitter;

@Singleton
public class RedisEventEmitter implements EventEmitter {

	public void emit(String event, Object data) {
		
	}
}
