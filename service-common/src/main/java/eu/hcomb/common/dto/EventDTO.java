package eu.hcomb.common.dto;

import java.lang.management.ManagementFactory;

import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EventDTO {

	protected String service;
	protected String instance;
	protected String method;
	protected String url;
	protected MultivaluedMap<String, String> queryParams;
	protected MultivaluedMap<String, String> requestHeaders;
	protected String remoteAddress;
	protected Long timestamp;
	protected Object body;
	
	public static EventDTO build(String serviceName, Object body, String method, String url, 
				MultivaluedMap<String, String> queryParameters, MultivaluedMap<String, String> requestHeaders, String remoteAddress){
		EventDTO ret = new EventDTO();
		ret.setService(serviceName);
		ret.setMethod(method);
		ret.setUrl(url);
		ret.setQueryParams(queryParameters);
		ret.setRequestHeaders(requestHeaders);
		ret.setInstance(ManagementFactory.getRuntimeMXBean().getName());
		ret.setRemoteAddress(remoteAddress);
		ret.setTimestamp(System.currentTimeMillis());
		ret.setBody(body);
		return ret;
	}

	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public MultivaluedMap<String, String> getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(MultivaluedMap<String, String> queryParams) {
		this.queryParams = queryParams;
	}
	public MultivaluedMap<String, String> getRequestHeaders() {
		return requestHeaders;
	}
	public void setRequestHeaders(MultivaluedMap<String, String> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	
}
