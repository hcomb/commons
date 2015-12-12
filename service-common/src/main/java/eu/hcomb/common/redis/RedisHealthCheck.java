package eu.hcomb.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.codahale.metrics.health.HealthCheck;

public class RedisHealthCheck extends HealthCheck {

	protected JedisPool pool;
	
	public RedisHealthCheck(JedisPool pool) {
		this.pool = pool;
	}
	
	@Override
	protected Result check() throws Exception {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String pong = jedis.ping();
            if ("PONG".equals(pong)) {
                return Result.healthy();
            }else{
                return Result.unhealthy("Expecting PONG, got: " + pong);
            }
        }catch(Exception e){
            return Result.unhealthy("Could not ping redis");
        }finally{
        	if(jedis!=null)
        		jedis.close();
        }
	}
}
