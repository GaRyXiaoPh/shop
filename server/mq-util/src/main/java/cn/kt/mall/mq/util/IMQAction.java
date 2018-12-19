package cn.kt.mall.mq.util;

import java.util.List;
import java.util.Map;

public interface IMQAction {

    public boolean add(String queue, Object msg);

    /**
     * 事务性发送消息
     * @author: shenxiaoping-549
     * @date: 2017/5/3 17:41
     * @param queue
     * @param msgList
     * @return
     */
    public boolean addTransactionalMsg(String queue, List<?> msgList);

    public boolean addWithProperties(String queue, Object msg, Map<String, Object> properties);

    public boolean simpleAdd(Object msg);

    public boolean update(String queue, Object msg);

    public boolean simpleUpdate(Object msg);

    public boolean remove(String queue, Object msg);

    public boolean simpleRemove(Object msg);

    public boolean sendTopic(String topic,Object msg);

    public boolean sendTopic(String topic, Object msg, String command);

    public boolean sendTopicWithProperties(String topic, Object msg, String command, Map<String, Object> properties);
}
