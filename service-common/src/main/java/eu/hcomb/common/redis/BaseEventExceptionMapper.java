package eu.hcomb.common.redis;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;

import eu.hcomb.common.service.EventEmitter;

public class BaseEventExceptionMapper {

	@Context
	protected UriInfo uriInfo;

	@Context
	protected HttpHeaders headers;
	
	@Context 
	protected HttpServletRequest request;
	
	@Inject
	protected EventEmitter eventEmitter;

}
