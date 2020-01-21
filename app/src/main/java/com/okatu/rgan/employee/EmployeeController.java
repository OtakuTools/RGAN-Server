package com.okatu.rgan.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @GetMapping("/hello")
    String helloWorld(){
        logger.error("log repo test");
        return "hello world";
    }
}
