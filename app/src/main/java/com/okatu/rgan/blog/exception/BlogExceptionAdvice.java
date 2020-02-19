package com.okatu.rgan.blog.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BlogExceptionAdvice {

    private static Logger logger = LoggerFactory.getLogger(BlogExceptionAdvice.class);

    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String entityNotFoundHandler(EntityNotFoundException exception){
        logger.error("entity not found in repository", exception);
        return exception.getMessage();
    }

    // HttpMessageNotReadableException是@RequestBody反序列化过程中抛出JsonParseException的包装，
    // 比方说JSON体多个逗号少个逗号
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String InvalidInputParameterHandler(HttpMessageNotReadableException exception){
        logger.error("Invalid request parameter", exception);
        return exception.getMessage();
    }

    // MethodArgumentNotValidException是违反了请求参数体中的@Valid注解约束而抛出的
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        logger.error("Invalid request parameter", exception);
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String backupExceptionHandler(Exception exception){
        logger.error("Backup controller exception handler", exception);
        return "";
    }

}