package eu.hcomb.common.cors;

public class CorsConfig {

	protected String methods;
	protected String origins;
	protected String headers;
	protected String allowCredentials;
	
	public String getMethods() {
		return methods;
	}
	public void setMethods(String methods) {
		this.methods = methods;
	}
	public String getOrigins() {
		return origins;
	}
	public void setOrigins(String origins) {
		this.origins = origins;
	}
	public String getHeaders() {
		return headers;
	}
	public void setHeaders(String headers) {
		this.headers = headers;
	}
	public String getAllowCredentials() {
		return allowCredentials;
	}
	public void setAllowCredentials(String allowCredentials) {
		this.allowCredentials = allowCredentials;
	}
	
}
