package com.spring.azure.springazurecloud.configuration.spring;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.service.resources.validation.GlobalValidationService;
import com.spring.azure.springazurecloud.service.payment.PaymentGatewayService;
import com.spring.azure.springazurecloud.service.cache.CacheService;
import com.spring.azure.springazurecloud.service.resources.acr.ContainerRegistryService;
import com.spring.azure.springazurecloud.service.resources.aks.AksService;
import com.spring.azure.springazurecloud.service.resources.apim.ApimService;
import com.spring.azure.springazurecloud.service.resources.group.ResourceGroupService;
import com.spring.azure.springazurecloud.service.resources.storage.StorageAccountService;
import com.spring.azure.springazurecloud.service.security.ClientService;
import com.spring.azure.springazurecloud.service.security.JwtService;
import com.spring.azure.springazurecloud.service.subscription.SubscriptionService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class SpringBeansConfiguration {
    @Bean(name = "UserDetailsService")
    @DependsOn({"PasswordEncoder", "CacheService", "PaymentGatewayService"})
    public ClientService clientService(
            @Qualifier("HibernateSessionFactory") SessionFactory sessionFactor,
            @Qualifier("HibernateTransactionTemplate") TransactionTemplate transactionTemplate,
            @Qualifier("PasswordEncoder") PasswordEncoder passwordEncoder,
            @Qualifier("CacheService") CacheService cacheService,
            @Qualifier("PaymentGatewayService") PaymentGatewayService paymentGatewayService
    ){
        return new ClientService(sessionFactor, transactionTemplate,passwordEncoder, cacheService,paymentGatewayService);
    }

    @Bean(name = "PasswordEncoder")
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean(name = "key")
    public String secretKey(){
        return Constants.SECURITY.KEY;
    }

    @Bean(name = "StripeKey")
    public String stripeKey(){return Constants.STRIPE.KEY;}

    @Bean(name = "JwtService")
    public JwtService jwtService(@Qualifier("key")String key,
                                 @Qualifier("UserDetailsService")ClientService clientService){
        return new JwtService(key, clientService);
    }

    @Bean(name = "PaymentGatewayService")
    public PaymentGatewayService paymentGatewayService(@Qualifier("StripeKey")String key){
        return new PaymentGatewayService(key);
    }

    @Bean(name = "CacheService")
    public CacheService cacheService(@Qualifier("CacheManager")CacheManager cacheManager){
        return new CacheService(cacheManager);
    }

    @Bean(name = "GlobalValidationService")
    public GlobalValidationService globalValidationService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                                           @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate){
        return new GlobalValidationService(sessionFactory, transactionTemplate);
    }

    @Bean(name = "SubscriptionService")
    public SubscriptionService subscriptionService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                                   @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate){
        return new SubscriptionService(sessionFactory, transactionTemplate);
    }

    @Bean(name = "ResourceGroupService")
    public ResourceGroupService resourceGroupService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                                     @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate,
                                                     @Qualifier("PaymentGatewayService")PaymentGatewayService paymentGatewayService){
        return new ResourceGroupService(sessionFactory, transactionTemplate, paymentGatewayService);
    }

    @Bean(name = "AksService")
    public AksService aksService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                 @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate,
                                 @Qualifier("GlobalValidationService")GlobalValidationService globalValidationService){
        return new AksService(sessionFactory, transactionTemplate, globalValidationService);
    }

    @Bean(name = "StorageAccountService")
    public StorageAccountService storageAccountService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                                       @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate,
                                                       @Qualifier("GlobalValidationService")GlobalValidationService validationService){
        return new StorageAccountService(sessionFactory, transactionTemplate, validationService);
    }

    @Bean(name = "ApimService")
    public ApimService apimService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                   @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate,
                                   @Qualifier("GlobalValidationService")GlobalValidationService globalValidationService){
        return new ApimService(sessionFactory, transactionTemplate, globalValidationService);
    }

    @Bean(name = "ContainerRegistryService")
    public ContainerRegistryService containerRegistryService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                   @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate,
                                                @Qualifier("GlobalValidationService")GlobalValidationService globalValidationService){
        return new ContainerRegistryService(sessionFactory, transactionTemplate, globalValidationService);
    }

}
