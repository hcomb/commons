package eu.hcomb.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;

public class RedisHealthCheck extends HealthCheck {

	@Inject
	JedisPool pool;
	
	@Override
	protected Result check() throws Exception {
		try {
			Jedis jedis = pool.getResource();
			String pong = jedis.ping();
            if ("PONG".equals(pong)) {
                return Result.healthy();
            }else{
                return Result.unhealthy("Expecting PONG, got: " + pong);
            }
        }catch(Exception e){
            return Result.unhealthy("Could not ping redis");
        }
	}
}
