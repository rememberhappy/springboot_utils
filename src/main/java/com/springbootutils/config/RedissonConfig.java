package com.springbootutils.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redisson 配置类
 * Created on 2018/6/19
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host:192.168.9.225}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private String port;

    @Value("${spring.redis.password:Wdcloud998}")
    private String password;

    @Value("${spring.redis.database:14}")
    private int database;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedissonClient getRedisson() {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password).setDatabase(database);
        return Redisson.create(config);
    }

    @Bean(name = {"redisTemplate"})
    public RedisTemplate<String, ?> getRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(this.redisConnectionFactory);
        return redisTemplate;
    }

}
