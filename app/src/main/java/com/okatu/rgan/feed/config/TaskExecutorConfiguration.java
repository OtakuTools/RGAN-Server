package com.okatu.rgan.feed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class TaskExecutorConfiguration {

//    private static ArrayBlockingQueue<> = new ArrayBlockingQueue<>();
//
//    @Bean
//    public TaskExecutor taskExecutor(){
//        TaskExecutor executor = new ThreadPoolExecutor();
//    }

//    @Bean
//    public TaskExecutor taskExecutor(){
//        Executor executor = new ThreadPoolExecutor(
//            4,
//            8,
//            20,
//            TimeUnit.MINUTES,
//            new ArrayBlockingQueue<>(2 << 16)
//        );
//
//        return (TaskExecutor) executor;
//    }
}
