package com.okatu.rgan.feed.config;

import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// https://spring.io/guides/gs/async-method/
@Configuration
public class TaskExecutorConfiguration{

    @Bean
    public Executor taskExecutor(){
        return new ThreadPoolExecutor(
            4,
            8,
            20,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(4096)
        );
    }

    @Bean
    public TaskScheduler taskScheduler(){
        TaskScheduler taskScheduler = new TaskSchedulerBuilder().poolSize(2).build();
        return taskScheduler;
    }
}
