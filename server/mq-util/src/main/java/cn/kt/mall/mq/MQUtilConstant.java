package cn.kt.mall.mq;

public class MQUtilConstant {

    private MQUtilConstant() {
    }

    public static final String MQ_QUEUE_TYPE = "type";// add del update
    /**
     * 重发次数存储在redis中key的前缀
     */
    public static final String REDELIVERY_REDIS_KEY_PREFIX = "mq:redelay:count:";
    /**
     * 循环冲重发redis中的key
     */
    public static final String RESEND_CYCLE_CNT_REDIS_KEY = "mq:resend:cycle:count:";
    /**
     * 是否循环重放的设置
     */
    public static final String RESEND_CYCLE_PROPERTY = "resend_cycle_propertes";
    /**
     * 用于跟踪重发的message id
     */
    public static final String RESEND_TRACE_MSG_ID = "resend_trace_msg_id";
}
