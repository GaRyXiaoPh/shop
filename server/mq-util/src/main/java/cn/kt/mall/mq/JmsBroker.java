package cn.kt.mall.mq;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.jms.Connection;
import javax.jms.MessageListener;

import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class JmsBroker{

    private PooledConnectionFactory jmsFactory;

    private JmsTemplate jmsTemplate;
    //支持事务配置
    private JmsTemplate jmsTemplateTransacted;
    private boolean normalWork = true ;
    // for listener
    private Map<String, Connection> connMap;
    private Map<String, MessageListener> messageListenerMap;
    private Map<String, String> durableTopicClientIdMap;

    public JmsBroker(){
        this.connMap = new HashMap<>(0);
        this.messageListenerMap = new HashMap<>(0);
        this.durableTopicClientIdMap = new HashMap<>(0);
    }

    public void start(){
        jmsFactory.start();
    }

    @PreDestroy
    public void stop(){
        jmsFactory.stop();
    }

    public PooledConnectionFactory getJmsFactory() {
        return jmsFactory;
    }
    public void setJmsFactory(PooledConnectionFactory jmsFactory) {
        this.jmsFactory = jmsFactory;
    }
    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public JmsTemplate getJmsTemplateTransacted() {
        return jmsTemplateTransacted;
    }

    public void setJmsTemplateTransacted(JmsTemplate jmsTemplateTransacted) {
        this.jmsTemplateTransacted = jmsTemplateTransacted;
    }

    public boolean isNormalWork() {
        return normalWork;
    }
    public void setNormalWork(boolean normalWork) {
        this.normalWork = normalWork;
    }

    public Map<String, Connection> getConnMap() {
        return connMap;
    }
    public void setConnMap(Map<String, Connection> connMap) {
        this.connMap = connMap;
    }
    public Map<String, MessageListener> getMessageListenerMap() {
        return messageListenerMap;
    }
    public void setMessageListenerMap(
            Map<String, MessageListener> messageListenerMap) {
        this.messageListenerMap = messageListenerMap;
    }

    public Map<String, String> getDurableTopicClientIdMap() {
        return durableTopicClientIdMap;
    }

    public void setDurableTopicClientIdMap(
            Map<String, String> durableTopicClientIdMap) {
        this.durableTopicClientIdMap = durableTopicClientIdMap;
    }

}
