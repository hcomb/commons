package eu.hcomb.common.dto;

import io.swagger.annotations.ApiModel;

import java.security.Principal;
import java.util.List;

@ApiModel(value="User", description="User Principal")
public class User implements Principal {

	protected String name;
	protected List<String> roles;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	
}
