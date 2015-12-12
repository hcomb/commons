package eu.hcomb.common.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import eu.hcomb.common.web.BaseConfig;

public class JedisModule extends AbstractModule {
	
	protected JedisPoolConfig poolConfig;
	protected JedisPool pool;
	
	protected BaseConfig configuration;
	
	public JedisModule(BaseConfig configuration) {
		this.configuration = configuration;
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
		}else{
			throw new RuntimeException("cannot configure jedis");
		}

	}
	
	@Provides
	public JedisPool getJedisPool(){
		return this.pool;
	}
}
