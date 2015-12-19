package eu.hcomb.common.service.impl;

import io.dropwizard.setup.Environment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.hcomb.common.redis.ManagedJedisPool;
import eu.hcomb.common.redis.RedisHealthCheck;
import eu.hcomb.common.service.EventEmitter;
import eu.hcomb.rrouter.client.RouterClient;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;

@Singleton
public class RedisEventEmitter implements EventEmitter {

	private static final Object BASE_SERVICE = "base-service";

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Inject
	RouterClient routerClient;
	
	List<RedisInstanceDTO> instances;
	Map<String,RouteDTO> routes;
	
	String serviceName;
	
	protected Map<String,JedisPool> pools = new HashMap<String,JedisPool>();
	
	
	public void emit(String event, Object data) {
		
	}
	
	public void init(String serviceName, Environment environment){
		log.debug("initializing event emitter");
		
		this.serviceName = serviceName;
		
		instances = routerClient.getAllInstances();
		List<RouteDTO> tmp = routerClient.getAllRoutes();
		routes = new HashMap<String,RouteDTO>();
		Set<String> managedInstances = new HashSet<String>();
		//TODO: filtro sul servizio?
		for (RouteDTO route : tmp) {
			if(route.getEventName()!=null 
					&& (route.getFrom().getKey().equals(serviceName) || route.getFrom().getKey().equals(BASE_SERVICE))
					&& route.getTo().getInstance()!=null){
				routes.put(route.getEventName(), route);
				log.debug("adding route: "+"on('"+route.getEventName()+"').from("
						+ToStringBuilder.reflectionToString(route.getFrom(),ToStringStyle.SHORT_PREFIX_STYLE)
						+ ").to("+
						ToStringBuilder.reflectionToString(route.getTo(),ToStringStyle.SHORT_PREFIX_STYLE)
						+ ")");
				managedInstances.add(route.getTo().getInstance());
			}
		}

		
		
		for (RedisInstanceDTO instance : instances) {
			if(managedInstances.contains(instance.getName())){
				JedisPoolConfig poolConfig = new JedisPoolConfig();
				poolConfig.setMinIdle(instance.getMinIdle());
				poolConfig.setMaxIdle(instance.getMaxIdle());
				poolConfig.setMaxTotal(instance.getMaxTotal());
		
				JedisPool pool = new JedisPool(poolConfig, instance.getHost(), instance.getPort(), 2000);

				log.debug("registering and managing redis pool: "+instance.getName());
				
				environment.lifecycle().manage(new ManagedJedisPool(pool));
				environment.healthChecks().register("redis-"+instance.getName(), new RedisHealthCheck(pool));
				
				setPool(instance.getName(), pool);
			}
		}

		log.debug("initialized event emitter with "+managedInstances.size()+" instances and "+routes.size()+" routes");
	}
	
	
	public JedisPool setPool(String key, final JedisPool pool) {
		String pkey = "router.pool."+key + ".active";
		return pools.put(key, pool);
	}

}
