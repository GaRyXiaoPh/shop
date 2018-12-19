package cn.kt.mall.mq.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JmsFactoryWrap {

    @Value("${mq.maxConnections:10}")
    private int maxConnections;
    @Value("${mq.idleTimeout:60000}")
    private int idleTimeout;
    @Value("${mq.expiryTimeout:600000}")
    private long expiryTimeout;
    @Autowired
    private ActiveMQConnectFactoryWrap activeMQConnectFactoryWrap;

    public int getMaxConnections() {
        return maxConnections;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public long getExpiryTimeout() {
        return expiryTimeout;
    }

    public ActiveMQConnectFactoryWrap getActiveMQConnectFactoryWrap() {
        return activeMQConnectFactoryWrap;
    }
}