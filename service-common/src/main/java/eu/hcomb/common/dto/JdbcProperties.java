package eu.hcomb.common.dto;

public class JdbcProperties {

    private String username;
    private String password;
    private String autoCommit;
    private String host;
    private String port;
    private String schema;
    
    private String healthCheckQuery;
    
	public String getHealthCheckQuery() {
		return healthCheckQuery;
	}
	public void setHealthCheckQuery(String healthCheckQuery) {
		this.healthCheckQuery = healthCheckQuery;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAutoCommit() {
		return autoCommit;
	}
	public void setAutoCommit(String autoCommit) {
		this.autoCommit = autoCommit;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
    
    
}
