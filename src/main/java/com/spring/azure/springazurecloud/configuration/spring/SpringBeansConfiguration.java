package com.spring.azure.springazurecloud.configuration.spring;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import com.spring.azure.springazurecloud.service.payment.PaymentGatewayService;
import com.spring.azure.springazurecloud.service.cache.CacheService;
import com.spring.azure.springazurecloud.service.resource.acr.ContainerRegistryService;
import com.spring.azure.springazurecloud.service.resource.acr.ContainerRegistryValidationService;
import com.spring.azure.springazurecloud.service.resource.aks.AksService;
import com.spring.azure.springazurecloud.service.resource.aks.AksValidationService;
import com.spring.azure.springazurecloud.service.resource.apim.ApimService;
import com.spring.azure.springazurecloud.service.resource.apim.ApimValidationService;
import com.spring.azure.springazurecloud.service.resource.group.ResourceGroupService;
import com.spring.azure.springazurecloud.service.resource.storage.StorageAccountService;
import com.spring.azure.springazurecloud.service.resource.storage.StorageAccountValidationService;
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

    @Bean(name = "AksValidationService")
    public AksValidationService aksValidationService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                                     @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate){
        return new AksValidationService(sessionFactory, transactionTemplate);
    }

    @Bean(name = "AksService")
    public AksService aksService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                 @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate,
                                 @Qualifier("AksValidationService")AksValidationService aksValidationService){
        return new AksService(sessionFactory, transactionTemplate, aksValidationService);
    }

    @Bean(name = "StorageAccountValidationService")
    public StorageAccountValidationService storageAccountValidationService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                                                           @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate){
        return new StorageAccountValidationService(sessionFactory, transactionTemplate);
    }

    @Bean(name = "StorageAccountService")
    public StorageAccountService storageAccountService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                                       @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate,
                                                       @Qualifier("StorageAccountValidationService")StorageAccountValidationService validationService){
        return new StorageAccountService(sessionFactory, transactionTemplate, validationService);
    }

    @Bean(name = "ApimValidationService")
    public ApimValidationService apimValidationService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                                                 @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate){
        return new ApimValidationService(sessionFactory, transactionTemplate);
    }

    @Bean(name = "ApimService")
    public ApimService apimService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                                       @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate,
                                                       @Qualifier("ApimValidationService")ApimValidationService validationService){
        return new ApimService(sessionFactory, transactionTemplate, validationService);
    }

    @Bean(name = "ContainerRegistryValidationService")
    public ContainerRegistryValidationService validationService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                                                    @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate){
        return new ContainerRegistryValidationService(sessionFactory, transactionTemplate);
    }

    @Bean(name = "ContainerRegistryService")
    public ContainerRegistryService apimService(@Qualifier("HibernateSessionFactory")SessionFactory sessionFactory,
                                   @Qualifier("HibernateTransactionTemplate")TransactionTemplate transactionTemplate,
                                   @Qualifier("ContainerRegistryValidationService")ContainerRegistryValidationService validationService){
        return new ContainerRegistryService(sessionFactory, transactionTemplate, validationService);
    }

}
