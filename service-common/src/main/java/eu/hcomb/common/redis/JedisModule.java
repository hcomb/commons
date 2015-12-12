package eu.hcomb.common.redis;

import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import eu.hcomb.common.service.RedisService;
import eu.hcomb.common.service.impl.RedisServiceJedisImpl;
import eu.hcomb.common.web.BaseConfig;

public class JedisModule extends AbstractModule {
	
	protected JedisPoolConfig poolConfig;
	protected JedisPool pool;
	
	protected BaseConfig configuration;
	protected Environment environment;
	
	public JedisModule(BaseConfig configuration, Environment environment) {
		this.configuration = configuration;
		this.environment = environment;
	}
	
	@Override
	protected void configure() {

		if(configuration instanceof JedisConfigurable){
			JedisConfigurable jedis = (JedisConfigurable)configuration;
		
			poolConfig = new JedisPoolConfig();
			poolConfig.setMinIdle(jedis.getJedisConfig().getMinIdle());
			poolConfig.setMaxIdle(jedis.getJedisConfig().getMaxIdle());
			poolConfig.setMaxTotal(jedis.getJedisConfig().getMaxTotal());
	
			pool = new JedisPool(poolConfig, jedis.getJedisConfig().getHost(), jedis.getJedisConfig().getPort(), Protocol.DEFAULT_TIMEOUT, jedis.getJedisConfig().getPassword());
			
			environment.lifecycle().manage(new ManagedJedisPool(pool));
			
			binder()
				.bind(RedisService.class)
				.to(RedisServiceJedisImpl.class);
			
		}else{
			throw new RuntimeException("cannot configure jedis");
		}

	}
	
	@Provides
	public JedisPool getJedisPool(){
		return this.pool;
	}
}
