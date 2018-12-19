package cn.kt.mall.mq.util;

import javax.jms.Message;

public interface MsgProcessor {

    void exec(Message msg) throws Exception;
}
