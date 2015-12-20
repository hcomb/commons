package eu.hcomb.common.service.impl;

import io.dropwizard.setup.Environment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import eu.hcomb.common.dto.EventDTO;
import eu.hcomb.common.redis.SendHandler;
import eu.hcomb.common.service.EventEmitter;
import eu.hcomb.common.service.RedisPoolContainer;
import eu.hcomb.common.service.RedisService;
import eu.hcomb.rrouter.dto.EndpointDTO;
import eu.hcomb.rrouter.dto.RedisInstanceDTO;
import eu.hcomb.rrouter.dto.RouteDTO;
import eu.hcomb.rrouter.service.RouterService;

@Singleton
public class RedisEventEmitter implements EventEmitter {

	private static final Object BASE_SERVICE = "base-service";

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Inject
	protected RouterService routerService;

	@Inject
	protected RedisService redisService;

	@Inject
	protected RedisPoolContainer redisPools;
	
	protected List<RedisInstanceDTO> instances;
	protected Map<String,RouteDTO> routes;
	
	protected String serviceName;
	
	protected ObjectMapper mapper = new ObjectMapper();
	
	protected SendHandler queueHandler = new SendHandler() {
		public void sendPayload(Jedis out, String destination, String payload) {
			out.lpush(destination, payload);
		}
	};
	
	protected SendHandler topicHandler = new SendHandler() {
		public void sendPayload(Jedis out, String destination, String payload) {
			out.publish(destination, payload);
		}
	};

	public void emit(String event, Object data) {
		try{
			log.debug("emitting event:"+event+" data: "+data);
			emitInternal(event, data, null, null, null, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void emit(String event, Object data, String method, 
			String url, MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> requestHeaders, String remoteAddress) {
		try{
			log.debug("emitting event:"+event+" data: "+data);
			emitInternal(event, data, method, url, queryParameters, requestHeaders, remoteAddress);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void emitInternal(String event, Object data, String method, String url, 
			MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> requestHeaders, String remoteAddress) throws JsonProcessingException {
		RouteDTO route = getRoute(event);
		if(route == null){
			log.warn("cannot find a route for event: "+event);
			return;
		}
		
		EndpointDTO endpoint = route.getTo();
		
		if(endpoint.getType().equals("queue")){
			redisService.send(redisPools.getPool(endpoint.getInstance()), null, endpoint.getKey(), getStringValue(data, method, url, queryParameters, requestHeaders, remoteAddress), queueHandler);
		}else if(endpoint.getType().equals("topic")){
			redisService.send(redisPools.getPool(endpoint.getInstance()), null, endpoint.getKey(), getStringValue(data, method, url, queryParameters, requestHeaders, remoteAddress), topicHandler);
		}else{
			log.warn("cannot handle type: " + endpoint.getType()+" for event: "+event);
		}
		
	}
	
	private String getStringValue(Object data, String method, String url, 
				MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> requestHeaders, String remoteAddress) throws JsonProcessingException {
			return mapper.writeValueAsString(EventDTO.build(serviceName, data, method, url, queryParameters, requestHeaders, remoteAddress));
	}

	private RouteDTO getRoute(String event) {
		for (String key : routes.keySet()) 
			if(event!=null && event.equals(key))
				return routes.get(key);
		return null;
	}

	public void init(String serviceName, Environment environment){
		log.debug("initializing event emitter");
		
		this.serviceName = serviceName;
		
		instances = routerService.getAllInstances();
		List<RouteDTO> tmp = routerService.getAllRoutes();
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
				redisPools.register(environment, instance);
			}
		}

		log.debug("initialized event emitter with "+managedInstances.size()+" instances and "+routes.size()+" routes");
	}


		
}
