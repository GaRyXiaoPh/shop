package cn.kt.mall.mq;

import java.io.Serializable;
import java.util.Map;

public class MQProperty implements Serializable {

    private static final long serialVersionUID = 5568559738845907384L;
    public static final String MQ_QUEUE_TYPE_ADD = "add";
    public static final String MQ_QUEUE_TYPE_DEL = "del";
    public static final String MQ_QUEUE_TYPE_UPDATE = "update";
    public static final String MQ_QUEUE_TYPE_MODIFY = "modify";
    private String typeParame;// Param
    private String queueName;// 队列名称
    private Object msg;// 消息体
    private long delayTime;// 消息延迟发送时间
    private Map<String,Object> params;

    public MQProperty(String queueName, Object msg, String typeParame) {
        this.queueName = queueName;
        this.msg = msg;
        this.typeParame = typeParame;
    }

    public String getTypeParame() {
        return typeParame;
    }

    public void setTypeParame(String typeParame) {
        this.typeParame = typeParame;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public MQProperty setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("MQProperty[");
        sb.append("typeParame='").append(typeParame).append('\'');
        sb.append(", queueName='").append(queueName).append('\'');
        sb.append(", msg=").append(msg);
        sb.append(", delayTime=").append(delayTime);
        sb.append(", params=").append(params);
        sb.append(']');
        return sb.toString();
    }
}
