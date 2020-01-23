package com.okatu.rgan.blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(BlogRepository repository){
        return args -> {
            logger.info(
                    "Preloading " + repository.save(new Blog("this is title", "fuck me"))
            );
        };
    }

}
