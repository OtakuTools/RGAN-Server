package com.okatu.rgan.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository){
        return args -> {
//            logger.info("Preloading " + repository.save(new Employee("Bilbo Baggins", "burglar")));
//            logger.info("Preloading" + repository.save(new Employee("Frodo Baggins", "thief")));
//            logger.info("Preloading" + repository.save(new Employee("中文o1aaa", "thief")));
        };
    }
}
