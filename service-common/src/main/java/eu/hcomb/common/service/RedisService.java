package eu.hcomb.common.service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import com.codahale.metrics.Meter;

import eu.hcomb.common.redis.SendHandler;


public interface RedisService {

	public abstract boolean publish(JedisPool pool, String channel, String message);

	public abstract boolean publish(JedisPool pool, String channel, Object message);

	public abstract String brpop(JedisPool pool, Meter meter, String queue);

	public abstract boolean send(JedisPool pool, Meter meter, String destination, String payload, SendHandler handler);

	public abstract void handleFailure(JedisPool pool, Meter meter, String destination, String payload, SendHandler handler, int maxRetry, int waitTime);

	public abstract boolean setupSubscription(JedisPool pool, JedisPubSub subscriber, String origin);

	public abstract void handleSubscriptionFailure(JedisPool pool, JedisPubSub subscriber, String origin, int maxRetry, int waitTime);
	
}
