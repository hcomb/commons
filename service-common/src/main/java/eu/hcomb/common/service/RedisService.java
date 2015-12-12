package eu.hcomb.common.service;

import redis.clients.jedis.JedisPubSub;


public interface RedisService {

	public abstract int getNumActive();

	public abstract Long publish(String channel, String message);

	public abstract Long publish(String channel, Object message);

	public abstract void subscribe(JedisPubSub subscriber, String... channels);
	
}
