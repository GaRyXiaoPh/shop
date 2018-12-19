package cn.kt.mall.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MQMgr {

    private static final Logger logger = LoggerFactory.getLogger(MQMgr.class);

    private static final String DELAY_TIME_MS = "delayTime";

    @Autowired
    private JmsClusterMgr jmsClusterMgr;

    public JmsClusterMgr getJmsClusterMgr() {
        return jmsClusterMgr;
    }

    public void setJmsClusterMgr(JmsClusterMgr jmsClusterMgr) {
        this.jmsClusterMgr = jmsClusterMgr;
    }

    public int send(MQProperty property) {
        Map<String, Object> msgPropertyMap = new HashMap<>();
        msgPropertyMap.put(MQUtilConstant.MQ_QUEUE_TYPE, property.getTypeParame());
        msgPropertyMap.put(DELAY_TIME_MS, property.getDelayTime());
        Map<String, Object> params = property.getParams();
        if(params != null){
            msgPropertyMap.putAll(params);
        }
        logger.info("发送MQ===>{}" , property.toString());
        return jmsClusterMgr.sendPstQueueMsg(property.getQueueName(), property.getMsg(), msgPropertyMap);
    }

    public int sendTopic(MQProperty property) {
        Map<String, Object> msgPropertyMap = new HashMap<>();
        msgPropertyMap.put(MQUtilConstant.MQ_QUEUE_TYPE, property.getTypeParame());
        msgPropertyMap.put(DELAY_TIME_MS, property.getDelayTime());
        logger.info("发送MQ===>{}" , property.toString());
        return jmsClusterMgr.sendTopicMsg(property.getQueueName(), property.getMsg(), msgPropertyMap);
    }

    /**
     *  发送事务消息，仅仅支持 消息插入
     * @param queue
     * @param data
     * @return
     */
    public int sendTransactionalMsg(String queue,List<?> data) {
        if(data == null || data.isEmpty()){
            logger.warn("none data to send");
            return 0;
        }
        Map<String, Object> msgPropertyMap = new HashMap<>(4);
        msgPropertyMap.put(MQUtilConstant.MQ_QUEUE_TYPE, MQProperty.MQ_QUEUE_TYPE_ADD);
        return jmsClusterMgr.sendNormalTransactionalMsg(data,queue,msgPropertyMap);
    }
}
