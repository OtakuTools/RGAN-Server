package com.okatu.rgan.blog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.okatu.rgan.blog.repository.BlogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class LoadDatabase {
    private static Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(BlogRepository repository){
        return args -> {
//            logger.info(
//                    "Preloading " + repository.save(new Blog("this is title", "fuck me", 0, 0))
//            );
        };
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "[\"a\", \"b\", \"a\"]";
        Set<String> set = objectMapper.readValue(json, new TypeReference<Set<String>>() {
        });

        System.out.println(set);
    }
}
