package com.hilda.common.config.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class SPHThreadPoolAutoConfiguration {

    @Bean
    public ThreadPoolExecutor createExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 8, 5L,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(5000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        return thread;
                    }
                });

        return executor;
    }

}
