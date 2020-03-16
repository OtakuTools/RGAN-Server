package com.okatu.rgan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class RganApplication {

	public static void main(String[] args) {
		SpringApplication.run(RganApplication.class, args);
	}

}
