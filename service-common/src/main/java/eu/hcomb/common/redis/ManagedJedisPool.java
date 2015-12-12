package eu.hcomb.common.redis;

import redis.clients.jedis.JedisPool;
import io.dropwizard.lifecycle.Managed;

public class ManagedJedisPool implements Managed {

	protected JedisPool pool;
	
	public ManagedJedisPool(JedisPool pool) {
		this.pool = pool;
	}
	
	public void start() throws Exception {
		
	}
	
	public void stop() throws Exception {
		pool.close();
	}
}
