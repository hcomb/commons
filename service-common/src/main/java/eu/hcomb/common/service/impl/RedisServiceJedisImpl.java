package eu.hcomb.common.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.codahale.metrics.Meter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import eu.hcomb.common.redis.SendHandler;
import eu.hcomb.common.service.RedisService;

public class RedisServiceJedisImpl implements RedisService {
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Inject
	protected ObjectMapper mapper;

	protected SendHandler publishToTopic = new SendHandler() {
		public void sendPayload(Jedis out, String destination, String payload) {
			out.publish(destination, payload);
		}
	};
	
	public boolean publish(JedisPool pool, String destination, String payload) {
		return this.send(pool, null, destination, payload, publishToTopic);
	}
	
	public void handleSubscriptionFailure(JedisPool pool, JedisPubSub subscriber, String origin, int maxRetry, int waitTime) {
		boolean ok = false;
		int max = maxRetry;
		while(!ok && max > 0){
			try{
				Thread.sleep(waitTime);
			}catch(Exception e){}
			max--;
			log.warn("retrying to subscribe, #" + max + ", origin:" + origin);
			ok = setupSubscription(pool, subscriber, origin);
		}
		
		if(!ok){
			log.error("cannot subscribe to origin: "+origin);
		}
		
	}

	public boolean setupSubscription(JedisPool pool, JedisPubSub subscriber, String origin){
		Jedis in = null;
		try {
			in = pool.getResource();
			in.subscribe(subscriber, origin);
			return true;
		}catch(JedisConnectionException e){
			pool.returnBrokenResource(in);
			in = null;
			log.warn("exception while subscribing to origin:" + origin + ", exception:"+e.getMessage());
			return false;
		}finally{
			if(in!=null)
				pool.returnResource(in);
		}
	}


	
	public void handleFailure(JedisPool pool, Meter meter, String destination, String payload, SendHandler handler, int maxRetry, int waitTime) {
		boolean ok = false;
		int max = maxRetry;
		while(!ok && max > 0){
			try{
				Thread.sleep(waitTime);
			}catch(Exception e){}
			max--;
			log.warn("retrying to forward, #" + max + ", destination:"+destination+", payload:" + payload);
			ok = send(pool, meter, destination, payload, handler);
		}
		
		if(!ok){
			log.error("cannot forward message, destination:"+destination+", payload:" + payload);
		}
		
	}

	public boolean send(JedisPool pool, Meter meter, String destination, String payload, SendHandler handler){
		if(payload == null)
			return true;
		
		Jedis out = null;
		try{
			out = pool.getResource();
			handler.sendPayload(out, destination, payload);
			if(meter!=null)
				meter.mark();
			return true;
		}catch(JedisConnectionException e){
			if(out!=null)
				pool.returnBrokenResource(out);
			out = null;
			log.warn("exception while sending message to destination:"+destination+": " + e.getMessage());
			return false;
		} finally {
			if(out!=null)
				pool.returnResource(out);
		}
	}

	
	public String brpop(JedisPool pool, Meter meter, String origin){
		Jedis in = null;
		String payload = null;
		try{
			in = pool.getResource();
			List<String> data = in.brpop(2, origin);
			if (data != null) {
				if(meter!=null)
					meter.mark();
				payload = data.get(1);
			}
		}catch(JedisConnectionException e){
			pool.returnBrokenResource(in);
			in = null;
			log.warn("exception while getting message from origin:" +origin+", exception:"+e.getMessage());
		} finally {
			if(in!=null)
				pool.returnResource(in);
		}
		return payload;
	}
	
	public boolean publish(JedisPool pool, String channel, Object message) {
		try {
			String string = mapper.writeValueAsString(message);
			return publish(pool, channel, string);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
		
}
