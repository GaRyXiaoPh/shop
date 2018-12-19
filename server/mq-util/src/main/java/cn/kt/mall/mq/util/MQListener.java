package cn.kt.mall.mq.util;

import cn.kt.mall.mq.JmsClusterMgr;
import cn.kt.mall.mq.MQUtilConstant;
import cn.kt.mall.mq.XMQCache;
import cn.kt.mall.mq.init.ActiveMQConnectFactoryWrap;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

public class MQListener implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(MQListener.class);
    private static final String ROLLBACK_MESSAGE = "处理消息失败，queue=>[{}]";

    private ApplicationContext ctx;
    @Autowired
    private JmsClusterMgr jmsClusterMgr;
    @Autowired
    private ActiveMQConnectFactoryWrap connInfo;
    @Autowired
    private MQAction mq;


    public ApplicationContext getCtx() {
        return ctx;
    }

    /**
     * 监听指定队列
     * @param queueName
     * @param callback
     */
    public void listener(final String queueName, final MsgProcessor callback) {
        jmsClusterMgr.registClusterQueueListener(queueName, new MessageListener() {

            @Override
            public void onMessage(Message message) {
                try {
                    callback.exec(message);
                    message.acknowledge();
                } catch (Rollback e) {
                    rollback(message);
                    LOG.warn(ROLLBACK_MESSAGE, queueName, e.getMessage());
                } catch (Exception e) {
                    rollback(message);
                    LOG.error(ROLLBACK_MESSAGE, queueName, e);
                }
            }
        });
    }

    /**
     * 监听指定队列
     * @param clientId ： 监听方ID， 一般为ip地址，主要方便查问题
     * @param queueName ： 队列名称
     * @param callback : 消息接收后的处理函数，如果异常的话，消息将仍然存储在MQ队列中
     */
    public void listener(String clientId, final String queueName, final MsgProcessor callback) {
        jmsClusterMgr.registClusterQueueListener(clientId, queueName, new MessageListener() {

            @Override
            public void onMessage(Message message) {
                try {
                    callback.exec(message);
                    message.acknowledge();
                } catch (Exception e) {
                    rollback(message);
                    LOG.error(ROLLBACK_MESSAGE, queueName, e);
                }
            }
        });
    }

    public void listener(String clientId, final String queueName, final DelayInfo delayInfo, final MsgProcessor callback) {
        jmsClusterMgr.registClusterQueueListener(clientId, queueName, new MessageListener() {

            @Override
            public void onMessage(Message message) {
                try {
                    callback.exec(message);
                    message.acknowledge();
                } catch (Exception e) {
                    delay(message, queueName, delayInfo);
                    LOG.error(ROLLBACK_MESSAGE, queueName, e);
                }
            }
        });
    }

    public void topicListener(final String queueName, final MsgProcessor callback) {
        jmsClusterMgr.registClusterTopicListener(queueName, new MessageListener() {

            @Override
            public void onMessage(Message message) {
                try {
                    callback.exec(message);
                    message.acknowledge();
                } catch (Exception e) {
                    rollback(message);
                    LOG.error(ROLLBACK_MESSAGE, queueName, e);
                }
            }
        });
    }

    public void rollback(Message message) {
        try {
            ActiveMQMessage atm = (ActiveMQMessage) message;
            ActiveMQConnection conn = atm.getConnection();
            String connectId = conn.getConnectionInfo().getConnectionId().getValue();
            Session session = XMQCache.getSessionById(connectId);
            if (session == null) {
                LOG.error("session is null");
                return;
            }
            session.recover();
        } catch (JMSException e) {
            LOG.error("fail to rollback message[{}]", e);
        }
    }

    public void delay(Message message, String queue, DelayInfo info) {
        try {
            boolean isContinue = this.isContinue(message, info);
            String msgId = message.getJMSMessageID();
            String msgTxt = ((TextMessage) message).getText();
            // String key = MQUtilConstant.REDELIVERY_REDIS_KEY_PREFIX + msgId
            // Integer cnt = getRedis().auomicInteger(key)
            int cnt = XMQCache.SNET_COUNT.incrementAndGet();
            if (isContinue && connInfo.getMaximumRedeliveries() <= cnt - 1) {
                message.acknowledge();
                Map<String, Object> params = new HashMap<>();
                params.put("AMQ_SCHEDULED_DELAY", info.getScheduledDelayMs());
                params.put("AMQ_SCHEDULED_PERIOD", info.getScheduledPeriod());
                params.put("AMQ_SCHEDULED_REPEAT", info.getScheduledRepeat());
                if (info.getScheduledCron() != null) {
                    params.put("AMQ_SCHEDULED_CRON", info.getScheduledCron());
                }
                params.put(MQUtilConstant.RESEND_TRACE_MSG_ID, msgId);
                mq.addWithProperties(queue, msgTxt, params);
                // 删除redis
            } else {
                rollback(message);
            }
        } catch (JMSException e) {
            LOG.error("jms exception ", e);
        }
    }

    /**
     * 取消对指定dest的监听
     * @since 1.0.0
     * @param dest
     */
    public void unRegistListener(final String dest) {
        jmsClusterMgr.unregistClusterListener(dest);
    }

    private boolean isContinue(Message message, DelayInfo info) {
        try {
            Integer cycleCount = info.getCycleCount();
            if (cycleCount == 0) {
                return true;
            }
            boolean isFirst = false;
            String msgId = message.getStringProperty(MQUtilConstant.RESEND_TRACE_MSG_ID);
            // 如果为null，说明是第一次延迟重发，所以直接返回true即可
            if (msgId == null) {
                isFirst = true;
                // msgId = message.getJMSMessageID()
            }
            // String msgKey = MQUtilConstant.RESEND_CYCLE_CNT_REDIS_KEY + msgId
            // int nextCount = getRedis().auomicInteger(msgKey)
            // redis.expireForHours(msgKey,MQHepler.getExpireHours(info))
            int nextCount = XMQCache.SNET_COUNT.incrementAndGet();
            if (isFirst) {
                return true;
            }
            return nextCount > cycleCount + 1;
        } catch (Exception e) {
            LOG.error("exception for isContinue", e);
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
}
