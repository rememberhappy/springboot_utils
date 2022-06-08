package com.springbootutils.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableAsync
public class ThreadPoolTaskConfig {
    /**
     * 核心线程数（默认线程数）
     */
    private static final int corePoolSize = 3;
    /**
     * 最大线程数
     */
    private static final int maxPoolSize = 3;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int keepAliveTime = 5;
    /**
     * 缓冲队列大小
     */
    private static final int queueCapacity = 100;
    /**
     * 线程池名前缀
     */
    private static final String threadNamePrefix = "hrss-exam-service-async-service-";

    @Bean("taskExecutor") // bean的名称，默认为首字母小写的方法名
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);

        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        return taskScheduler;
    }
}
