package eu.hcomb.common.service.impl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import eu.hcomb.common.service.RedisService;

public class RedisServiceJedisImpl implements RedisService {

	@Inject
	protected JedisPool pool;
	
	@Inject
	protected ObjectMapper mapper;
	
	public Long publish(String channel, Object message) {
		try {
			String string = mapper.writeValueAsString(message);
			return publish(channel, string);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Long publish(String channel, String message){
		Jedis jedis = null;
		try {
			  jedis = pool.getResource();
			  return jedis.publish(channel, message);
		} finally {
		  if (jedis != null) {
		    jedis.close();
		  }
		}
	}

	public void subscribe(JedisPubSub subscriber, String... channels) {
		Jedis jedis = null;
		try {
			  jedis = pool.getResource();
			  jedis.subscribe(subscriber, channels);
		} finally {
		  if (jedis != null) {
		    jedis.close();
		  }
		}
	}
	
	public int getNumActive() {
		return pool.getNumActive();
	}
	
	
}
