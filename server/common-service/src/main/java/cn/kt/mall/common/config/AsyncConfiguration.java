package cn.kt.mall.common.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步配置
 * Created by wqt on 2018/1/3.
 */
@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    /** 本机的 cpu 核心数 */
    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(PROCESSORS);
        executor.setMaxPoolSize(PROCESSORS << 3);        // 左移三位等同于 * 8
        executor.setQueueCapacity(PROCESSORS << 2);      // 左移一位等同于 * 4
        executor.setThreadNamePrefix("task-executor-");  // 线程名字的前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());    // 当新的线程任务无法进入线程池是，使用主线程执行
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
