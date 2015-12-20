package eu.hcomb.common.service.impl;

import io.dropwizard.setup.Environment;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import eu.hcomb.common.redis.ManagedJedisPool;
import eu.hcomb.common.redis.RedisHealthCheck;
import eu.hcomb.common.service.RedisPoolContainer;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;

@Singleton
public class RedisPoolContainerImpl implements RedisPoolContainer {

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Inject
	protected MetricRegistry registry;
	
	protected Map<String,JedisPool> pools = new HashMap<String,JedisPool>();

	protected Map<String,Gauge<Long>> gauges = new HashMap<String,Gauge<Long>>();

	public JedisPool getPool(String key) {
		return pools.get(key);
	}

	public JedisPool setPool(String key, JedisPool value) {
		return pools.put(key, value);
	}
	
	public void register(Environment environment, RedisInstanceDTO instance) {

		JedisPool check = pools.get(instance.getName());
		if(check!=null)
			return;
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMinIdle(instance.getMinIdle());
		poolConfig.setMaxIdle(instance.getMaxIdle());
		poolConfig.setMaxTotal(instance.getMaxTotal());

		JedisPool pool = new JedisPool(poolConfig, instance.getHost(), instance.getPort(), 2000);

		log.debug("registering and managing redis pool: "+instance.getName());
		
		environment.lifecycle().manage(new ManagedJedisPool(pool, instance.getName()));
		environment.healthChecks().register("redis-"+instance.getName(), new RedisHealthCheck(pool));
		
		pools.put(instance.getName(), pool);
		registerGauge(instance.getName(), pool);
	}

	public void registerGauge(String key, final JedisPool pool) {
		
		String pkey = "router.pool."+key + ".active";
		Gauge<Long> gauge = gauges.get(pkey);
		if(gauge == null){
			log.info("registering gauge metric on pool "+key);
			gauge = new Gauge<Long>() {
				public Long getValue() {
					return new Long(pool.getNumActive());
				}
			};
			
			registry.register(pkey, gauge);
			gauges.put(pkey, gauge);
		}
	}
}
