package eu.hcomb.common.redis;

import io.dropwizard.lifecycle.Managed;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.JedisPool;

public class ManagedJedisPool implements Managed {

	protected Log log = LogFactory.getLog(this.getClass());
	
	protected JedisPool pool;
	protected String name;
	
	public ManagedJedisPool(JedisPool pool, String name) {
		this.name = name;
		this.pool = pool;
	}
	
	public void start() throws Exception {
		log.debug("starting jedis pool: "+name);
	}
	
	public void stop() throws Exception {
		log.debug("stopping jedis pool: "+name);
		pool.close();
	}
}
