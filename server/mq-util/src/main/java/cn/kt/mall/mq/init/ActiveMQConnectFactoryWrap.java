package cn.kt.mall.mq.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQConnectFactoryWrap {

    @Value("${mq.conn.brokerURLs}")
    private String brokerURLs;
    @Value("${mq.conn.username}")
    private String userName;
    @Value("${mq.conn.password}")
    private String password;
    @Value("${mq.conn.useAsyncSend:true}")
    private boolean useAsyncSend;
    @Value("${mq.conn.alwaysSessionAsync:false}")
    private boolean alwaysSessionAsync;
    @Value("${mq.conn.optimizeAcknowledge:true}")
    private boolean optimizeAcknowledge;
    @Value("${mq.conn.producerWindowSize:1024000}")
    private int producerWindowSize;
    @Value("${mq.conn.max.retry:6}")
    private int maximumRedeliveries;
    @Value("${mq.conn.retry.delay.seconds:2}")
    private int initialRedeliveryDelay;

    public String getBrokerURLs() {
        return brokerURLs;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isUseAsyncSend() {
        return useAsyncSend;
    }

    public boolean isAlwaysSessionAsync() {
        return alwaysSessionAsync;
    }

    public boolean isOptimizeAcknowledge() {
        return optimizeAcknowledge;
    }

    public int getProducerWindowSize() {
        return producerWindowSize;
    }

    public int getMaximumRedeliveries() {
        return maximumRedeliveries;
    }

    public int getInitialRedeliveryDelay() {
        return initialRedeliveryDelay;
    }
}
