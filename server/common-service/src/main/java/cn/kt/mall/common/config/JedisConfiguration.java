package cn.kt.mall.common.config;

import cn.kt.mall.common.cache.MybatisRedisCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**Jedis(Redis)配置类
 * Created by jerry on 2017/7/14.
 */
@Configuration
@EnableCaching
public class JedisConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;
    //哨兵的集群id
    @Value("${spring.redis.sentinel.master}")
    private String sentinelMaster;
    //哨兵节点
    @Value("${spring.redis.sentinel.nodes}")
    private String sentinelNodes;


    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.pool.max-wait}")
    private int maxWait;

    /**
     * redis数据源
     * @return JedisPoolConfig
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxActive);
        config.setMaxWaitMillis(maxWait);
        return config;
    }

    /**
     * Spring-redis连接池管理工厂
     * @return JedisConnectionFactory
   /*  *//*
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setPassword(password);
        factory.setPoolConfig(jedisPoolConfig);
        return factory;
    }*/

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(sentinelMaster);

        for (Properties p : parseNodes()) {
            sentinelConfig.sentinel(p.getProperty("ip"), Integer.parseInt(p.getProperty("port")));
        }


        JedisConnectionFactory factory = new JedisConnectionFactory(sentinelConfig);
        factory.setPassword(password);
        factory.setPoolConfig(jedisPoolConfig());

        return factory;
    }

    private List<Properties> parseNodes() {
        String[] nodeHosts = sentinelNodes.trim().split(",");

        List<Properties> hostPropertiesList = new ArrayList<>();
        for (String nodeHost : nodeHosts) {
            String[] passwordAndHost = nodeHost.split("@");
            String[] ipAndPort = passwordAndHost[1].split(":");

            Properties p = new Properties();
            p.setProperty("ip", ipAndPort[0]);
            p.setProperty("port", ipAndPort[1]);
            p.setProperty("password", passwordAndHost[0]);

            hostPropertiesList.add(p);
        }

        return hostPropertiesList;
    }


    /**
     * 使用中间类解决RedisCache.jedisConnectionFactory的静态注入，从而使MyBatis实现第三方缓存
     * @param jedisConnectionFactory JedisConnectionFactory
     * @return RedisCacheTransfer
     */
    @Bean
    public RedisCacheTransfer redisCacheTransfer(JedisConnectionFactory jedisConnectionFactory) {
        return new RedisCacheTransfer(jedisConnectionFactory);
    }

    @Bean
    public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(jedisConnectionFactory);
        return template;
    }

    class RedisCacheTransfer {

        public RedisCacheTransfer(JedisConnectionFactory jedisConnectionFactory) {
            MybatisRedisCache.setJedisConnectionFactory(jedisConnectionFactory);
        }
    }
}


