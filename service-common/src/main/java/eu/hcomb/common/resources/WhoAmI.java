package eu.hcomb.common.resources;

import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import eu.hcomb.common.dto.User;

@Api
@Path("/whoami")
@Produces(MediaType.APPLICATION_JSON)
public class WhoAmI {

    @GET
    @Timed
    @RolesAllowed("USER")
    @ApiOperation(value="Get logged in user", notes = "This can only be done by a logged in user.")
    public User whoami(@Auth User user) {
    	return user;
    }

}
