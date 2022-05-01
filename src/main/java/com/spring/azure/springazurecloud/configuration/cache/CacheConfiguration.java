package com.spring.azure.springazurecloud.configuration.cache;

import com.spring.azure.springazurecloud.configuration.constants.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class CacheConfiguration {

    @Bean(name = "JedisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory connectionFactory =
                new JedisConnectionFactory();

        connectionFactory.setHostName(Constants.CACHE.CACHE_HOST);
        connectionFactory.setPort(Constants.CACHE.CACHE_PORT);
        connectionFactory.setUsePool(Constants.CACHE.CACHE_POOl);

        return connectionFactory;
    }

    @Bean(name = "RedisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(
            @Qualifier("JedisConnectionFactory") JedisConnectionFactory connectionFactory
    ){
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean(name = "CacheManager")
    public CacheManager cacheManager(
            @Qualifier("RedisTemplate") RedisTemplate<Object, Object> redisTemplate
    )
    {
        return new RedisCacheManager(redisTemplate);
    }
}
