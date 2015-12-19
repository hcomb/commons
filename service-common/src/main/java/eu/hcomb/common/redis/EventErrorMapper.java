package eu.hcomb.common.redis;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class EventErrorMapper extends BaseEventExceptionMapper implements ExceptionMapper<Error>{
	
	public Response toResponse(Error exception) {
		
		eventEmitter.emit("error",
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
