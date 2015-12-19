package eu.hcomb.common.redis;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class EventExceptionMapper extends BaseEventExceptionMapper implements ExceptionMapper<Exception>{

	public Response toResponse(Exception exception) {
		
		eventEmitter.emit("exception",
				exception,
				request.getMethod(),
				uriInfo.getRequestUri().toString(),
				uriInfo.getQueryParameters(),
				headers.getRequestHeaders(),
				request.getRemoteAddr()
			);
			
		
		return Response.status(500).entity(exception.getMessage()).type(MediaType.APPLICATION_JSON).build();
	}

}
