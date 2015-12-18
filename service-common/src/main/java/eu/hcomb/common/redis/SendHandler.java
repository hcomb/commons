package eu.hcomb.common.redis;

import redis.clients.jedis.Jedis;

public interface SendHandler {
	
	public abstract void sendPayload(Jedis out, String destination, String payload);
	
}
