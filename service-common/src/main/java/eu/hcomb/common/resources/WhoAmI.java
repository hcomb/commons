package eu.hcomb.common.resources;

import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import eu.hcomb.common.dto.User;

@Path("/whoami")
@Produces(MediaType.APPLICATION_JSON)
public class WhoAmI {

    @GET
    @Timed
    @RolesAllowed("user")
    public User login(@Auth User user) {
    	return user;
    }

}
