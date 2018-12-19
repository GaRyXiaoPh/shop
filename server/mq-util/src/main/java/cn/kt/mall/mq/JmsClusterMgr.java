package cn.kt.mall.mq;

import cn.kt.mall.mq.init.MQWrapHelper;
import cn.kt.mall.mq.util.ErrorMQ;
import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JmsClusterMgr {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsClusterMgr.class);
    public static final String EXPIRED_TIME = "expiredTime";
    private int brokerNum;
    private List<JmsBroker> jmsBrokerList;
    @Autowired
    private MQWrapHelper helper;

    @PostConstruct
    public void init() {
        jmsBrokerList = helper.getJmsBrokers();
        brokerNum = jmsBrokerList.size();
    }

    private JmsBroker getJmsBroker() {
        JmsBroker jmsBroker = null;
        double r = Math.random();
        int random = (int) (r * 10);
        jmsBroker = jmsBrokerList.get(random % brokerNum);
        if (jmsBroker.isNormalWork()) {
            return jmsBroker;
        }
        // 随机选中直失败后，按顺序选择第一个可用的broker
        for (int i = 0; i < brokerNum; i++) {
            jmsBroker = jmsBrokerList.get(i);
            if (jmsBroker.isNormalWork()) {
                return jmsBroker;
            }
        }
        // 整个集群崩溃，返回null
        LOGGER.error(" All brokers go down, please check Active MQ ");
        return null;
    }

    /**
     * 转换消息
     * @param msg
     * @param mqSession
     * @return
     * @throws JMSException
     */
    private Message transformMsg(Object msg, Session mqSession) throws JMSException {
        Message message = null;
        if (msg instanceof String || msg instanceof Integer || msg instanceof Long) {
            // message = mqSession.createTextMessage((String) msg);
            message = mqSession.createTextMessage(String.valueOf(msg));
        } else {
            message = mqSession.createObjectMessage((Serializable) msg);
        }
        return message;
    }

    /**
     * 设置message属性
     * @param message
     * @param msgPropertyMap
     * @return
     * @throws JMSException
     */
    private Map<String, Object> initMsgProperties(Message message, Map<String, Object> msgPropertyMap)
            throws JMSException {
        if (msgPropertyMap == null || msgPropertyMap.isEmpty()) {
            return null;
        }
        Map<String, Object> resMap = new HashMap<>();
        // 判断消息是否延迟发送
        Object delayTimeSetting = msgPropertyMap.get("delayTime");
        long delayTime = delayTimeSetting != null ? Long.parseLong(delayTimeSetting.toString()) : 0;
        if (delayTime > 0) {
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delayTime);
        }
        // 如果有，处理消息参数，处理完参数需要立即从map移除该参数，便于后续仅留下消息属性
        long expiredTime = -1;
        Object expiredTimeObj = msgPropertyMap.get(JmsClusterMgr.EXPIRED_TIME);
        if (expiredTimeObj != null) {
            expiredTime = ((Long) expiredTimeObj).longValue();
            msgPropertyMap.remove(JmsClusterMgr.EXPIRED_TIME);
        }
        resMap.put(EXPIRED_TIME, expiredTime);
        // setting other properties
        Iterator<Map.Entry<String, Object>> iterator = msgPropertyMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null || StringUtils.isBlank(key)) {
                continue;
            }
            if (value instanceof String) {
                message.setStringProperty(key, (String) value);
            } else if (value instanceof Integer) {
                message.setIntProperty(key, (Integer) value);
            } else if (value instanceof Long) {
                message.setLongProperty(key, (Long) value);
            } else if (value instanceof Boolean) {
                message.setBooleanProperty(key, (Boolean) value);
            } else if (value instanceof Double) {
                message.setDoubleProperty(key, (Double) value);
            } else {
                LOGGER.warn("ignore property[{}] setting : unknown data type[{}] for activeMQ message", key, value
                        .getClass().getCanonicalName());
            }
        }
        return resMap;
    }

    /**
     * 发送普通消息公用方法
     * @param msg
     * @param queue
     * @param destName
     * @param persitent
     * @param msgPropertyMap
     * @return
     */
    private int doSendNormalMsg(final Object msg, final boolean queue, final String destName, final boolean persitent,
                                final Map<String, Object> msgPropertyMap) {
        JmsBroker jmsBroker = null;
        for (int i = 0; i < brokerNum; i++) {
            try {
                jmsBroker = this.getJmsBroker();
                if (jmsBroker == null) {
                    LOGGER.error("JMS 集群中没有可用broker");
                    return 0;
                }
                // 避免设置默认队列名时造成无法发送消息
                Destination destination = null;
                jmsBroker.getJmsTemplate().execute(destination, new ProducerCallback() {

                    public Object doInJms(Session mqSession, MessageProducer producer) throws JMSException {
                        return commandSendMsg(mqSession, producer, msg, msgPropertyMap, queue, destName, persitent);
                    }
                });
                return 1;
            } catch (NullPointerException | ClassCastException e) {
                LOGGER.error("发送消息出现异常，忽略该消息", e);
                break;
            } catch (Exception e) {
                synchronized (jmsBroker) {
                    if (jmsBroker != null) {
                        jmsBroker.setNormalWork(false);
                    }
                }
                // 扔出线程,不断检测损坏的节点,如果修复,即时修改状态,便于后续加入集群中
                new SenderResumeThread(jmsBroker).start();
                if (jmsBroker != null) {
                    ActiveMQConnectionFactory acf = (ActiveMQConnectionFactory) jmsBroker.getJmsFactory()
                            .getConnectionFactory();
                    LOGGER.error("JMS集群节点 " + acf.getBrokerURL() + " 出现异常：" + e.toString() + " , 修复线程已启动", e);
                }
            }
        }
        return 0;
    }

    private Object commandSendMsg(Session mqSession, MessageProducer producer, Object msg,
                                  Map<String, Object> msgPropertyMap, boolean queue, String destName, boolean persitent) throws JMSException {
        // 将消息转换成Message对象
        Message message = transformMsg(msg, mqSession);
        // 初始化参数设置，并返回后续代码需要使用的参数
        Map<String, Object> resMap = initMsgProperties(message, msgPropertyMap);
        // 过期时间设置
        long expiredTime = -1;
        if (resMap != null && resMap.size() > 0) {
            expiredTime = (Long) resMap.get(EXPIRED_TIME);
        }
        // 发送消息
        if (expiredTime > 0) {
            producer.send(queue ? mqSession.createQueue(destName) : mqSession.createTopic(destName), message,
                    persitent ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY,
                    expiredTime);
        } else {
            producer.setDeliveryMode(persitent ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT);
            producer.send(queue ? mqSession.createQueue(destName) : mqSession.createTopic(destName), message);
        }
        return null;
    }

    /**
     * 普通消息发送
     * @param msg
     * @param msgPropertyMap 消息对象 注意：消息对象没有提供Integer类型处理，如果需要Object值赋值null即可
     * @return 0:发送失败，1：发送成功
     */
    private int sendNormalMsg(final Object msg, final boolean queue, final String destName, final boolean persitent,
                              final Map<String, Object> msgPropertyMap) {
        return this.doSendNormalMsg(msg, queue, destName, persitent, msgPropertyMap);
    }

    /**
     * 发送事务消息
     * @param msgList
     * @param queueName
     * @param msgPropertyMap
     * @return
     */
    public int sendNormalTransactionalMsg(final List<?> msgList, final String queueName,
                                          final Map<String, Object> msgPropertyMap) {
        int cnt = 0;
        for (int i = 0; i < brokerNum; i++) {
            JmsBroker jmsBroker = null;
            try {
                jmsBroker = this.getJmsBroker();
                if (jmsBroker == null) {
                    LOGGER.error("All brokers are broken.please check the MQ Cluster! ");
                    return 0;
                }
                // 如果消息回滚失败，则不会通过broker轮询重试，防止出现重复数据。
                cnt = doSendNormalMsgWithTransacted(jmsBroker, msgList, queueName, msgPropertyMap);
                break;
            } catch (NullPointerException | ClassCastException e) {
                LOGGER.error("发送消息出现异常{}，忽略该消息", e.getMessage(), e);
                break;
            } catch (Exception e) {
                synchronized (jmsBroker) {
                    if (jmsBroker != null) {
                        jmsBroker.setNormalWork(false);
                    }
                }
                // 扔出线程,不断检测损坏的节点,如果修复,即时修改状态,便于后续加入集群中
                new SenderResumeThread(jmsBroker).start();
                if (jmsBroker != null) {
                    ActiveMQConnectionFactory acf = (ActiveMQConnectionFactory) jmsBroker.getJmsFactory()
                            .getConnectionFactory();
                    LOGGER.error("JMS集群节点 [{}] 出现异常：{} , 修复线程已启动", acf.getBrokerURL(), e.getMessage(), e);
                }
            }
        }
        return cnt;
    }

    /**
     * 带事务发送的JMS
     * @param msgList
     * @param queueName
     * @param msgPropertyMap
     * @return
     */
    private int doSendNormalMsgWithTransacted(JmsBroker jmsBroker, final List<?> msgList, final String queueName,
                                              final Map<String, Object> msgPropertyMap) throws ErrorMQ {
        int cnt = 0;
        Session session = null;
        try {
            if (msgList == null || msgList.isEmpty()) {
                LOGGER.warn("==========none data to send. topic is [] =============", queueName);
                return 0;
            }
            Connection connection = helper.createConn((ActiveMQConnectionFactory) jmsBroker.getJmsFactory()
                    .getConnectionFactory());
            connection.start();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue queue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(queue);
            for (int i = 0; i < msgList.size(); i++) {
                Object s = msgList.get(i);
                Message message = this.transformMsg(s, session);
                // Map<String, Object> resMap =
                this.initMsgProperties(message, msgPropertyMap);
                this.commandSendMsg(session, producer, s, msgPropertyMap, true, queueName, true);
            }
            session.commit();
            cnt = msgList.size();
        } catch (Exception e) {
            String msgJson = JSONObject.toJSONString(msgList);
            LOGGER.error("fail to send batch jms message to queue[{}],msg==>[],", msgJson, queueName, e);
            if (session != null) {
                try {
                    session.rollback();
                } catch (JMSException ex) {
                    LOGGER.error("fail to rollback jms message==>[{}], please clear the dirty data manually.", msgJson,
                            ex);
                    return 0;
                }
            }
            throw new ErrorMQ(e);
        }
        return cnt;
    }

    /**
     * 发送p2p非持久化消息
     * @param queueName p2p队列名称
     * @param msg xml消息
     * @return
     */
    public int sendQueueMsg(String queueName, Object msg) {
        return this.sendNormalMsg(msg, true, queueName, false, null);
    }

    public int sendQueueMsg(String queueName, Object msg, Map<String, Object> msgPropertyMap) {
        return this.sendNormalMsg(msg, true, queueName, false, msgPropertyMap);
    }

    /**
     * 发送p2p持久化消息
     * @param queueName p2p队列名称
     * @param msg xml消息
     * @return
     */
    public int sendPstQueueMsg(String queueName, Object msg) {
        return this.sendNormalMsg(msg, true, queueName, true, null);
    }

    public int sendPstQueueMsg(String queueName, Object msg, Map<String, Object> msgPropertyMap) {
        return this.sendNormalMsg(msg, true, queueName, true, msgPropertyMap);
    }

    /**
     * 发送PubSub非持久化消息
     * @param topicName pubSub主题名称
     * @param msg xml消息
     * @return
     */
    public int sendTopicMsg(String topicName, Object msg) {
        return this.sendNormalMsg(msg, false, topicName, false, null);
    }

    /**
     * 发送PubSub非持久化消息，带消息属性
     * @param topicName pubSub主题名称
     * @param msg xml消息
     * @param msgPropertyMap 消息属性，消息属性值支持String,Long,Boolean,Double
     * @return
     */
    public int sendTopicMsg(String topicName, Object msg, Map<String, Object> msgPropertyMap) {
        return this.sendNormalMsg(msg, false, topicName, false, msgPropertyMap);
    }

    /**
     * 发送PubSub持久化消息
     * @param topicName pubSub主题名称
     * @param msg xml消息
     * @return
     */
    public int sendPstTopicMsg(String topicName, Object msg) {
        return this.sendNormalMsg(msg, false, topicName, true, null);
    }

    /**
     * 发送PubSub持久化消息，带消息属性
     * @param topicName pubSub主题名称
     * @param msg xml消息
     * @param msgPropertyMap 消息属性，消息属性值支持String,Long,Boolean,Double
     * @return
     */
    public int sendPstTopicMsg(String topicName, Object msg, Map<String, Object> msgPropertyMap) {
        return this.sendNormalMsg(msg, false, topicName, true, msgPropertyMap);
    }

    // 发送端修复线程
    class SenderResumeThread extends Thread {

        private JmsBroker jmsBroker;

        public SenderResumeThread(JmsBroker jmsBroker) {
            this.jmsBroker = jmsBroker;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    jmsBroker.getJmsTemplate().browse(new BrowserCallback() {

                        public Object doInJms(Session arg0, QueueBrowser arg1) throws JMSException {
                            arg1.getQueue();
                            return null;
                        }
                    });
                    synchronized (jmsBroker) {
                        jmsBroker.setNormalWork(true);
                    }
                    ActiveMQConnectionFactory acf = (ActiveMQConnectionFactory) jmsBroker.getJmsFactory()
                            .getConnectionFactory();
                    LOGGER.info("已修复JMS Broker " + acf.getBrokerURL() + " , 已经加入集群");
                    break;
                } catch (Exception e) {
                    LOGGER.error("修复 JMS Broker 线程错误, Sleeping：{}", e);
                    synchronized (jmsBroker) {
                        try {
                            jmsBroker.wait(1000L * 10);
                        } catch (Exception e2) {
                            //
                        }
                    }
                }
            }
        }
    }

    /**
     * 针对某个Desnation注册JMS集群的消息监听
     * @param clientId
     * @param queue
     * @param destNameWithParam
     * @param messageSelector
     * @param messageListener
     * @return 注册成功的集群节点个数
     */
    private int registClusterListener(String clientId, boolean queue, String destNameWithParam, String messageSelector,
                                      MessageListener messageListener) {
        int ret = 0;
        JmsBroker jmsBroker = null;
        Connection conn = null;
        for (int i = 0; i < brokerNum; i++) {
            try {
                jmsBroker = jmsBrokerList.get(i);
                conn = helper.createConn((ActiveMQConnectionFactory) jmsBroker.getJmsFactory().getConnectionFactory());
                jmsBroker.getConnMap().put(destNameWithParam, conn);
                jmsBroker.getMessageListenerMap().put(destNameWithParam, messageListener);
                if (clientId != null) {
                    conn.setClientID(clientId);
                    if (!queue) {
                        jmsBroker.getDurableTopicClientIdMap().put(destNameWithParam, clientId);
                    }
                }
                Session se = conn.createSession(false, Session.CLIENT_ACKNOWLEDGE);
                MessageConsumer messageConsumer = null;
                if (clientId != null && !queue) {
                    if (messageSelector == null) {
                        messageConsumer = se.createDurableSubscriber(new ActiveMQTopic(destNameWithParam), clientId);
                    } else {
                        messageConsumer = se.createDurableSubscriber(new ActiveMQTopic(destNameWithParam), clientId,
                                messageSelector, true);
                    }
                } else {
                    if (messageSelector == null) {
                        messageConsumer = se.createConsumer(queue ? new ActiveMQQueue(destNameWithParam)
                                : new ActiveMQTopic(destNameWithParam));
                    } else {
                        messageConsumer = se.createConsumer(queue ? new ActiveMQQueue(destNameWithParam)
                                : new ActiveMQTopic(destNameWithParam), messageSelector);
                    }
                }
                // cache session
                cacheSession(conn, se);
                messageConsumer.setMessageListener(messageListener);
                conn.setExceptionListener(new ConnExceptionListener(jmsBroker, queue, destNameWithParam));
                conn.start();
                ret++;
            } catch (Exception e) {
                removeSessionCache(conn);
                new ReceiverResumeThread(jmsBroker, queue, destNameWithParam).start();
                LOGGER.error("注册JMS集群节点：[{}] , 失败，监听修复线程已启动", e.getMessage(), e);
            }
        }
        LOGGER.info("对JMS集群中所有Broker注册Destination[{}]的监听器完毕,并成功注册了[{}]个监听器", destNameWithParam, ret);
        return ret;
    }

    // 监听端修复线程
    class ReceiverResumeThread extends Thread {

        private JmsBroker jmsBroker;
        private boolean queue;
        private String destNameWithParam;

        public ReceiverResumeThread(JmsBroker jmsBroker, boolean queue, String destNameWithParam) {
            this.jmsBroker = jmsBroker;
            this.queue = queue;
            this.destNameWithParam = destNameWithParam;
        }

        @Override
        public void run() {
            Connection conn = null;
            String clientId = jmsBroker.getDurableTopicClientIdMap().get(destNameWithParam);
            while (true) {
                try {
                    conn = helper.createConn((ActiveMQConnectionFactory) jmsBroker.getJmsFactory()
                            .getConnectionFactory());
                    if (clientId != null) {
                        conn.setClientID(clientId);
                    }
                    jmsBroker.getConnMap().put(destNameWithParam, conn);
                    Session se = conn.createSession(false, Session.CLIENT_ACKNOWLEDGE);
                    MessageConsumer messageConsumer = null;
                    if (clientId != null) {
                        messageConsumer = se.createDurableSubscriber(new ActiveMQTopic(destNameWithParam), clientId);
                    } else {
                        messageConsumer = se.createConsumer(queue ? new ActiveMQQueue(destNameWithParam)
                                : new ActiveMQTopic(destNameWithParam));
                    }
                    // must cache it before conn.start()
                    cacheSession(conn, se);
                    messageConsumer.setMessageListener(jmsBroker.getMessageListenerMap().get(destNameWithParam));
                    conn.setExceptionListener(new ConnExceptionListener(jmsBroker, queue, destNameWithParam));
                    conn.start();
                    ActiveMQConnectionFactory acf = (ActiveMQConnectionFactory) jmsBroker.getJmsFactory()
                            .getConnectionFactory();
                    LOGGER.info("已修复对JMS Broker[{}]中Destination的监听", acf.getBrokerURL(), destNameWithParam);
                    break;
                } catch (Exception e) {
                    synchronized (jmsBroker) {
                        try {
                            removeSessionCache(conn);
                            jmsBroker.wait(1000L * 10);
                        } catch (Exception e2) {
                            //
                        }
                    }
                }
            }
        }
    }

    // 监听异常处理
    class ConnExceptionListener implements ExceptionListener {

        private JmsBroker jmsBroker;
        private boolean queue;
        private String destNameWithParam;

        public ConnExceptionListener(JmsBroker jmsBroker, boolean queue, String destNameWithParam) {
            this.jmsBroker = jmsBroker;
            this.queue = queue;
            this.destNameWithParam = destNameWithParam;
        }

        public void onException(JMSException exception) {
            try {
                jmsBroker.getConnMap().get(destNameWithParam).close();
            } catch (Exception e) {
                //
            }
            // 启动监听修复线程
            new ReceiverResumeThread(jmsBroker, queue, destNameWithParam).start();
            ActiveMQConnectionFactory acf = (ActiveMQConnectionFactory) jmsBroker.getJmsFactory()
                    .getConnectionFactory();
            LOGGER.error("JMS集群节点:[{}] 上对Destination[{}] 的监听器出现故障,监听修复线程已启动", acf.getBrokerURL(), destNameWithParam,
                    exception);
        }
    }

    /**
     * 注册集群中指定p2p的监听，系统初始化调用
     * @param queueNameWithParam，可带参数的队列名称，通常就是一个单纯的队列名
     * @param messageListener
     * @return
     */
    public int registClusterQueueListener(String queueNameWithParam, MessageListener messageListener) {
        return this.registClusterListener(null, true, queueNameWithParam, null, messageListener);
    }

    public int registClusterQueueListener(String clientId, String queueNameWithParam, MessageListener messageListener) {
        return this.registClusterListener(clientId, true, queueNameWithParam, null, messageListener);
    }

    /**
     * 注册集群中指定PubSub的非持久监听，系统初始化调用
     * @param topicNameWithParam，可带参数的队列名称，通常就是一个单纯的队列名
     * @param messageListener
     * @return
     */
    public int registClusterTopicListener(String topicNameWithParam, MessageListener messageListener) {
        return this.registClusterListener(null, false, topicNameWithParam, null, messageListener);
    }

    /**
     * 注册集群中指定PubSub的非持久监听，带消息选择器，系统初始化调用
     * @param topicNameWithParam，可带参数的队列名称，通常就是一个单纯的队列名
     * @param messageSelector 消息选择器表达式，如果不需要请调用上面的方法
     * @param messageListener
     * @return
     */
    public int registClusterTopicListener(String topicNameWithParam, String messageSelector,
                                          MessageListener messageListener) {
        return this.registClusterListener(null, false, topicNameWithParam, messageSelector, messageListener);
    }

    /**
     * 注册集群中指定PubSub的持久性监听，系统初始化调用
     * @param uniqueClientId，消费端标示，必须在整个集群唯一
     * @param topicNameWithParam，可带参数的队列名称，通常就是一个单纯的队列名
     * @param messageListener
     * @return
     */
    public int registClusterDurableTopicListener(String uniqueClientId, String topicNameWithParam,
                                                 MessageListener messageListener) {
        return this.registClusterListener(uniqueClientId, false, topicNameWithParam, null, messageListener);
    }

    /**
     * 注册集群中指定PubSub的持久性监听，带消息选择器 , 系统初始化调用
     * @param uniqueClientId，消费端标示，必须在整个集群唯一
     * @param topicNameWithParam，可带参数的队列名称，通常就是一个单纯的队列名
     * @param messageSelector 消息选择器表达式，如果不需要请调用上面的方法
     * @param messageListener
     * @return
     */
    public int registClusterDurableTopicListener(String uniqueClientId, String topicNameWithParam,
                                                 String messageSelector, MessageListener messageListener) {
        return this.registClusterListener(uniqueClientId, false, topicNameWithParam, messageSelector, messageListener);
    }

    /**
     * 销毁所有的JMS监听，系统关闭时调用
     */
    public void unregistClusterListener() {
        Connection conn = null;
        Map<String, Connection> connMap = null;
        Iterator<String> destNameWithParamIter = null;
        String destNameWithParam = null;
        for (JmsBroker jmsBroker : jmsBrokerList) {
            connMap = jmsBroker.getConnMap();
            destNameWithParamIter = connMap.keySet().iterator();
            while (destNameWithParamIter.hasNext()) {
                try {
                    destNameWithParam = destNameWithParamIter.next();
                    conn = connMap.get(destNameWithParam);
                    conn.stop();
                    conn.close();
                } catch (Exception e) {
                    LOGGER.error("context", e);
                }
            }
            jmsBroker.getConnMap().clear();
            jmsBroker.getDurableTopicClientIdMap().clear();
            jmsBroker.getMessageListenerMap().clear();
        }
        XMQCache.cleanSession();
    }

    /**
     * 销毁指定目标（queue或者topic）的JMS监听
     */
    public void unregistClusterListener(String destName) {
        Connection conn = null;
        Map<String, Connection> connMap = null;
        Iterator<String> destNameWithParamIter = null;
        String destNameWithParam = null;
        for (JmsBroker jmsBroker : jmsBrokerList) {
            connMap = jmsBroker.getConnMap();
            destNameWithParamIter = connMap.keySet().iterator();
            while (destNameWithParamIter.hasNext()) {
                try {
                    destNameWithParam = destNameWithParamIter.next();
                    if (destNameWithParam.equals(destName)) {
                        conn = connMap.get(destNameWithParam);
                        removeSessionCache(conn);
                        if (conn != null) {
                            conn.stop();
                            conn.close();
                        }
                        break;
                    }
                } catch (Exception e) {
                    LOGGER.error("context", e);
                }
            }
        }
    }

    /**
     * remove
     * @param conn
     */
    private void removeSessionCache(Connection conn) {
        if (conn != null) {
            XMQCache.removeSession(getConnId(conn));
        }
    }

    /**
     * cache session
     * @param conn
     * @param se
     */
    private void cacheSession(Connection conn, Session se) {
        XMQCache.putSession(getConnId(conn), se);
    }

    private String getConnId(Connection conn) {
        ActiveMQConnection connection = (ActiveMQConnection) conn;
        return connection.getConnectionInfo().getConnectionId().getValue();
    }
}
