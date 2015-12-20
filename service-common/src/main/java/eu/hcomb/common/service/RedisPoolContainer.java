package eu.hcomb.common.service;

import io.dropwizard.setup.Environment;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import redis.clients.jedis.JedisPool;

public interface RedisPoolContainer {

	public abstract JedisPool setPool(String key, JedisPool value);

	public abstract JedisPool getPool(String key);

	public abstract void register(Environment environment, RedisInstanceDTO instance);

}
