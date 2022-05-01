package com.spring.azure.springazurecloud.configuration.spring;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class JdbcBeansConfiguration {

    @Bean(name = "JdbcDatasource")
    public DataSource jdbcDatasource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(Constants.DATABASE.JDBC_URL);
        dataSource.setUsername(Constants.DATABASE.JDBC_USERNAME);
        dataSource.setPassword(Constants.DATABASE.JDBC_PASSWORD);
        dataSource.setDriverClassName(Constants.DATABASE.JDBC_DRIVER);
        return dataSource;
    }

    @Bean(name = "JdbcTemplate")
    public JdbcTemplate jdbcTemplate(
            @Qualifier("JdbcDatasource") DataSource dataSource
    )
    {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "JdbcPlatformTransactionManager")
    public PlatformTransactionManager jdbcPlatformTransactionManager(
            @Qualifier("JdbcDatasource")DataSource dataSource
    )
    {
        DataSourceTransactionManager transactionManager =
                new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }


    @Bean(name = "JdbcPlatformTransactionManager")
    public TransactionTemplate jdbcTransactionTemplate(
            @Qualifier("JdbcPlatformTransactionManager") PlatformTransactionManager transactionManager
    )
    {
        return new TransactionTemplate(transactionManager);
    }
}
