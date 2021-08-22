package com.iyzico.challenge.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfiguration {
    @Bean
    public ThreadPoolTaskExecutor taskExecuter(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(2);
        taskExecutor.setQueueCapacity(250);
        taskExecutor.setCorePoolSize(2);
        return taskExecutor;
    }
}
