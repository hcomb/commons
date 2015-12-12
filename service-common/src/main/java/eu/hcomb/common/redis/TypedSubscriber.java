package eu.hcomb.common.redis;

import java.io.IOException;

import redis.clients.jedis.JedisPubSub;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class TypedSubscriber<T> extends JedisPubSub {
	
	protected ObjectMapper mapper = new ObjectMapper();
	protected Class<T> klass;
	
	public TypedSubscriber(Class<T> klass) {
		this.klass = klass;
	}
	
	@Override
	public void onMessage(String channel, String message) {
		
		try {
			T object = mapper.readValue(message, klass);
			onMessage(channel, object);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	
	}

	public abstract void onMessage(String channel, T object);

	@Override
	public void onPMessage(String pattern, String channel, String message) {
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
	}
	
}
