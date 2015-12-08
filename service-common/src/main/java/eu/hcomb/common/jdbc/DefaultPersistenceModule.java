package eu.hcomb.common.jdbc;

import java.util.Properties;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;

import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import eu.hcomb.common.web.BaseConfig;

public abstract class DefaultPersistenceModule extends MyBatisModule {
	
	protected BaseConfig configuration;
	
	public DefaultPersistenceModule(BaseConfig configuration) {
		this.configuration = configuration;
	}
	
	protected void setup(){
		bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);

        Names.bindProperties(binder(), createProperties());
	}

	protected Properties createProperties() {
		if(configuration instanceof JdbcConfigurable){
			JdbcConfigurable jdbc = (JdbcConfigurable)configuration;
	        Properties myBatisProperties = new Properties();
	        myBatisProperties.setProperty("mybatis.environment.id", "test");
	        
	        myBatisProperties.setProperty("JDBC.username", jdbc.getJdbcConfig().getUsername());
	        myBatisProperties.setProperty("JDBC.password", jdbc.getJdbcConfig().getPassword());
	        myBatisProperties.setProperty("JDBC.autoCommit", jdbc.getJdbcConfig().getAutoCommit());
	        myBatisProperties.setProperty("JDBC.host", jdbc.getJdbcConfig().getHost());
	        myBatisProperties.setProperty("JDBC.port", jdbc.getJdbcConfig().getPort());
	        myBatisProperties.setProperty("JDBC.schema", jdbc.getJdbcConfig().getSchema());
	        return myBatisProperties;
		}else{
			throw new RuntimeException("cannot configure jdbc");
		}

	}
	
	@Provides
	@Named("healthcheck.query")
	public String getHealthCheckQuery(){
		if(configuration instanceof JdbcConfigurable){
			JdbcConfigurable jdbc = (JdbcConfigurable)configuration;
			return jdbc.getJdbcConfig().getHealthCheckQuery();
		}else {
			throw new RuntimeException("cannot configure jdbc");
		}
			
	}
}
