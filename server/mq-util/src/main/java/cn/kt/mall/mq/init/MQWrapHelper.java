package cn.kt.mall.mq.init;

import cn.kt.mall.mq.JmsBroker;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;


public class MQWrapHelper implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(MQWrapHelper.class);
    private ApplicationContext context;
    @Autowired
    private ActiveMQConnectFactoryWrap mqConnectFactoryWrap;
    @Autowired
    private JmsFactoryWrap factoryWrap;
    @Autowired
    private JmsTemplateWrap templateWrap;

    public List<ActiveMQConnectionFactory> getActiveMQConnectFactories(String[] urls) {
        int len = urls.length;
        List<ActiveMQConnectionFactory> res = new ArrayList<>(len);
        String userName = mqConnectFactoryWrap.getUserName();
        String pwd = mqConnectFactoryWrap.getPassword();
        boolean useAsyncSend = mqConnectFactoryWrap.isUseAsyncSend();
        boolean alwaysSessionAsync = mqConnectFactoryWrap.isAlwaysSessionAsync();
        boolean optimizeAcknowledge = mqConnectFactoryWrap.isOptimizeAcknowledge();
        int producerWindowSize = mqConnectFactoryWrap.getProducerWindowSize();
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(mqConnectFactoryWrap.getMaximumRedeliveries());
        redeliveryPolicy.setRedeliveryDelay((long)mqConnectFactoryWrap.getInitialRedeliveryDelay() * 1000);
        for (int i = 0; i < len; i++) {
            ActiveMQConnectionFactory connectFactory = new ActiveMQConnectionFactory();
            connectFactory.setUserName(userName);
            connectFactory.setPassword(pwd);
            connectFactory.setRedeliveryPolicy(redeliveryPolicy);
            connectFactory.setBrokerURL(urls[i]);
            connectFactory.setUseAsyncSend(useAsyncSend);
            connectFactory.setAlwaysSessionAsync(alwaysSessionAsync);
            connectFactory.setOptimizeAcknowledge(optimizeAcknowledge);
            connectFactory.setProducerWindowSize(producerWindowSize);
            connectFactory.setTrustAllPackages(true);
            res.add(connectFactory);
        }
        return res;
    }

    public List<PooledConnectionFactory> getPooledConnFactory() {
        String urlStrs = mqConnectFactoryWrap.getBrokerURLs();
        LOG.info("ActiveMQ cluster urls==>{}", urlStrs);
        String[] urls = urlStrs.split(",");
        int len = urls.length;
        List<ActiveMQConnectionFactory> activeMQConnectionFactories = getActiveMQConnectFactories(urls);
        List<PooledConnectionFactory> factories = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            PooledConnectionFactory factory = new PooledConnectionFactory();
            factory.setMaxConnections(factoryWrap.getMaxConnections());
            factory.setExpiryTimeout(factoryWrap.getExpiryTimeout());
            factory.setIdleTimeout(factoryWrap.getIdleTimeout());
            factory.setConnectionFactory(activeMQConnectionFactories.get(i));
            factories.add(factory);
        }
        return factories;
    }

    public JmsTemplate getJmsTemplate(PooledConnectionFactory factory, boolean isTransacted) {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(factory);
        template.setPubSubDomain(templateWrap.isPubSubDomain());
        template.setDefaultDestinationName(templateWrap.getDefaultDstName());
        template.setSessionTransacted(isTransacted);
        return template;
    }

    public List<JmsBroker> getJmsBrokers() {
        List<PooledConnectionFactory> pooledConnectionFactories = getPooledConnFactory();
        List<JmsBroker> cluster = new ArrayList<>(pooledConnectionFactories.size());
        for (int i = 0; i < pooledConnectionFactories.size(); i++) {
            JmsBroker broker = context.getBean(JmsBroker.class);
            PooledConnectionFactory jmsFactory = pooledConnectionFactories.get(i);
            broker.setJmsFactory(jmsFactory);
            broker.setJmsTemplate(getJmsTemplate(jmsFactory, false));
            broker.setJmsTemplateTransacted(getJmsTemplate(jmsFactory, true));
            broker.start();
            cluster.add(broker);
        }
        return cluster;
    }

    public Connection createConn(ActiveMQConnectionFactory factory) throws JMSException {
        Connection conn = null;
        ActiveMQConnectionFactory connFactory = new ActiveMQConnectionFactory();
        connFactory.setBrokerURL(factory.getBrokerURL());
        connFactory.setPassword(factory.getPassword());
        connFactory.setUserName(factory.getUserName());
        connFactory.setUseAsyncSend(factory.isUseAsyncSend());
        connFactory.setAlwaysSessionAsync(factory.isAlwaysSessionAsync());
        connFactory.setOptimizeAcknowledge(factory.isOptimizeAcknowledge());
        connFactory.setProducerWindowSize(factory.getProducerWindowSize());
        connFactory.setRedeliveryPolicy(factory.getRedeliveryPolicy());
        connFactory.setTrustAllPackages(true);
        conn = connFactory.createConnection();
        return conn;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
