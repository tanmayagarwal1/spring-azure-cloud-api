package com.spring.azure.springazurecloud.configuration.spring;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.service.security.ClientService;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan("com.spring.azure.springazurecloud")
@EnableWebMvc
@EnableTransactionManagement
public class HibernateBeansConfiguration {
    @Bean(name = "HibernateDataSource")
    public DataSource hibernateDatasource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(Constants.DATABASE.HIBERNATE_URL);
        dataSource.setUsername(Constants.DATABASE.HIBERNATE_USERNAME);
        dataSource.setPassword(Constants.DATABASE.HIBERNATE_PASSWORD);
        dataSource.setDriverClassName(Constants.DATABASE.HIBERNATE_DRIVER);
        return dataSource;
    }

    @Bean(name = "HibernateLocalSessionFactoryBean")
    public LocalSessionFactoryBean hibernateLocalSessionFactoryBean(
            @Qualifier("HibernateDataSource") DataSource dataSource
    )
    {
            LocalSessionFactoryBean sfb = new LocalSessionFactoryBean();
            sfb.setDataSource(dataSource);
            sfb.setPackagesToScan(Constants.DATABASE.HIBERNATE_COMPONENT_CLASS);
            sfb.setHibernateProperties(getHibernateProperties());
            return sfb;
    }

    @Bean(name = "HibernateSessionFactory")
    public SessionFactory hibernateSessionFactory(
            @Qualifier("HibernateLocalSessionFactoryBean")LocalSessionFactoryBean sfb
    )
    {
        return sfb.getObject();
    }

    @Bean(name = "HibernateTransactionManager")
    public PlatformTransactionManager hibernatePlatformTransactionManager(
            @Qualifier("HibernateSessionFactory") SessionFactory sessionFactory
    ){
        HibernateTransactionManager hibernateTransactionManager =
                new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactory);
        return hibernateTransactionManager;
    }

    @Bean(name = "HibernateTransactionTemplate")
    public TransactionTemplate hibernateTransactionTemplate(
            @Qualifier("HibernateTransactionManager") PlatformTransactionManager transactionManager
    )
    {
        return new TransactionTemplate(transactionManager);
    }

    private Properties getHibernateProperties(){
        Properties properties = new Properties();
        properties.setProperty(Constants.DATABASE.HIBERNATE_DIALECT_PROPERTY_SPECIFIER,
                Constants.DATABASE.HIBERNATE_DIALECT);
        properties.setProperty(Constants.DATABASE.HIBERNATE_SHOW_SQL_SPECIFIER,
                Constants.DATABASE.HIBERNATE_SHOW_SQL);

        return properties;
    }


}
