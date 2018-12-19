package cn.kt.mall.front.config;

import cn.kt.mall.front.listener.ActiveMqListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Component
public class SystemInit {

    private static final Logger logger = LoggerFactory.getLogger(SystemInit.class);


    /**
     * 初始化加载系统几倍的的配置或者初始化的数据
     */
    @PostConstruct
    public void init() {
        initCfgs();
        initDatas();
        //目前不适用ActiveMQ
        //initJms();
        initOthers();
    }

    /**
     * 销毁系统级别的资源
     */
    @PreDestroy
    public void destroy() {
        destroyJms();
    }

    /**
     * 初始化的系统配置
     */
    private void initCfgs() {
    }

    /**
     * 初始化的数据
     */
    private void initDatas() {
    }




    private void destroyJms() {
        logger.info("销毁JMS....");
        new Thread(() -> {
        }).start();
    }

    /**
     * 初始化其他资源
     */
    private void initOthers() {
    }
}
