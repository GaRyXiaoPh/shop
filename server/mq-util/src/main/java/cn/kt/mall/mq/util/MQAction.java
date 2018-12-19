package cn.kt.mall.mq.util;

import cn.kt.mall.mq.MQMgr;
import cn.kt.mall.mq.MQProperty;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MQAction implements IMQAction {

    private static final Logger LOG = LoggerFactory.getLogger(MQAction.class);
    private static final String SIMPLE_ADD_NULL = "msg is null ,please check it.";
    private static final String ADD_NULL = "queue[{}] or msg[{}] is null,please check";
    @Autowired
    private MQMgr mq;

    @Override
    public boolean add(String queue, Object msg) {
        if (msg == null || queue == null) {
            LOG.warn(ADD_NULL, queue, msg);
            return false;
        }
        return doSend(queue, msg, MQProperty.MQ_QUEUE_TYPE_ADD);
    }

    @Override
    public boolean addWithProperties(String queue, Object msg, Map<String, Object> properties) {
        if (msg == null || queue == null) {
            LOG.warn(ADD_NULL, queue, msg);
            return false;
        }
        return doSend(queue, msg, MQProperty.MQ_QUEUE_TYPE_ADD, properties);
    }

    @Override
    public boolean addTransactionalMsg(String queue, List<?> msgList) {
        if (msgList == null || queue == null) {
            LOG.warn(ADD_NULL, queue, msgList);
            return false;
        }
        return mq.sendTransactionalMsg(queue, msgList) > 0;
    }

    /**
     * 发送消息，消息队列名和对象名相同， 如果系统中存在相对对象名就有问题，不能使用此方法
     * @author: shenxiaoping-549
     * @date: 2017/4/27 17:45
     * @param msg
     * @return
     */
    @Override
    public boolean simpleAdd(Object msg) {
        if (msg == null) {
            LOG.warn(SIMPLE_ADD_NULL);
            return false;
        }
        return add(getQueue(msg), msg);
    }

    @Override
    public boolean update(String queue, Object msg) {
        if (msg == null || queue == null) {
            LOG.warn(ADD_NULL, queue, msg);
            return false;
        }
        return doSend(queue, msg, MQProperty.MQ_QUEUE_TYPE_UPDATE);
    }

    @Override
    public boolean simpleUpdate(Object msg) {
        if (msg == null) {
            LOG.warn(SIMPLE_ADD_NULL);
            return false;
        }
        return update(getQueue(msg), msg);
    }

    @Override
    public boolean remove(String queue, Object msg) {
        if (msg == null || queue == null) {
            LOG.warn(ADD_NULL, queue, msg);
            return false;
        }
        return doSend(queue, msg, MQProperty.MQ_QUEUE_TYPE_DEL);
    }

    @Override
    public boolean simpleRemove(Object msg) {
        if (msg == null) {
            LOG.warn(SIMPLE_ADD_NULL);
            return false;
        }
        return remove(getQueue(msg), msg);
    }

    private String getQueue(Object msg) {
        String queue = msg.getClass().getSimpleName();
        if (msg instanceof Collection) {
            Collection<?> msgTemp = (Collection<?>) msg;
            if (msgTemp.isEmpty()) {
                throw new Rollback("none data to send");
            }
            Iterator<?> it = msgTemp.iterator();
            while (it.hasNext()) {
                Object s = it.next();
                if (s != null) {
                    String full = s.getClass().getCanonicalName();
                    if (full.startsWith("com.hhly")) {
                        queue = s.getClass().getSimpleName();
                        break;
                    } else {
                        throw new Rollback("the msg is not a com.hhly.**** object , so you should not use simpeXXX "
                                + "method! please invoke sending method included queue name parameter.");
                    }
                }
            }
        }
        return queue;
    }

    private boolean doSend(String queue, Object msg, String actionType) {
        return doSend(queue, msg, actionType, null);
    }

    private boolean doSend(String queue, Object msg, String actionType, Map<String, Object> params) {
        MQProperty property = new MQProperty(queue, msg, actionType);
        if (params != null) {
            // 目前只处理delayTime 属性，其它属性后期加上。
            try {
                Object delayTime = params.get("delayTime");
                if (delayTime != null) {
                    property.setDelayTime(Long.valueOf(delayTime.toString()));
                }
                // Map<String,Object> paramMap = new HashMap()
                // paramMap.put("mqProperties", params.get("mqProperties"))
                property.setParams(params);
            } catch (Exception e) {
                LOG.error("fail to init properties ==>{}", JSONObject.toJSONString(params), e);
            }
        }
        return mq.send(property) > 0;
    }

    private boolean doSendTopic(String topic, Object msg, String actionType) {
        return doSendTopic(topic, msg, actionType, null);
    }

    private boolean doSendTopic(String topic, Object msg, String actionType, Map<String, Object> params) {
        MQProperty property = new MQProperty(topic, msg, actionType);
        if (params != null) {
            // 目前只处理delayTime 属性，其它属性后期加上。
            try {
                property.setDelayTime(Long.valueOf(params.get("delayTime").toString()));
            } catch (Exception e) {
                LOG.error("fail to init properties ==>{}", JSONObject.toJSONString(params), e);
            }
        }
        return mq.sendTopic(property) > 0;
    }

    @Override
    public boolean sendTopic(String topic, Object msg, String command) {
        if (msg == null || StringUtils.isEmpty(topic)) {
            LOG.warn("params error:topic[{}], msg[{}], please check", topic, msg, command);
            return false;
        }
        return doSendTopic(topic, msg, command);
    }

    @Override
    public boolean sendTopicWithProperties(String topic, Object msg, String command, Map<String, Object> properties) {
        if (msg == null || StringUtils.isEmpty(topic)) {
            LOG.warn("params error:topic[{}], msg[{}], please check", topic, msg, command);
            return false;
        }
        return doSendTopic(topic, msg, command, properties);
    }

    @Override
    public boolean sendTopic(String topic, Object msg) {
        return sendTopic(topic, msg, null);
    }
}