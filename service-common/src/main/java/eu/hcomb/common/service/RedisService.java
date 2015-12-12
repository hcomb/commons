package eu.hcomb.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import redis.clients.jedis.JedisPubSub;


public interface RedisService {

	public abstract int getNumActive();

	public abstract Long publish(String channel, String message);

	public abstract Long publish(String channel, Object message) throws JsonProcessingException;

	public abstract void subscribe(String channel, JedisPubSub subscriber);
	
}
