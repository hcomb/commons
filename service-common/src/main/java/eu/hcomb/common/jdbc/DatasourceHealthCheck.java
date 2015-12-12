package eu.hcomb.common.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DatasourceHealthCheck extends HealthCheck {

	@Inject
	protected DataSource datasource;

	@Inject
	@Named("healthcheck.query")
	protected String query;
		
	@Override
	protected Result check() throws Exception {
		Connection conn = null;
		try {
			conn = datasource.getConnection(); 
			conn.createStatement().executeQuery(query);
			return Result.healthy();
		} catch (Exception e) {
			e.printStackTrace();
			return Result.unhealthy(e.getMessage());
		}finally{
			conn.close();
		}
	}
	
}
