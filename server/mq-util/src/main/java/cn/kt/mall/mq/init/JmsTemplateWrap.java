package cn.kt.mall.mq.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JmsTemplateWrap {

    @Value("${mq.template.pubSubDomain:false}")
    private boolean pubSubDomain;
    private String defaultDstName = "ping_queue";

    public boolean isPubSubDomain() {
        return pubSubDomain;
    }

    public String getDefaultDstName() {
        return defaultDstName;
    }
}