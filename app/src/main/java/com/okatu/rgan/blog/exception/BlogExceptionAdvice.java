package com.okatu.rgan.blog.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ResponseBody
    @ExceptionHandler({InvalidInputParameterException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String InvalidInputParameterHandler(Exception exception){
        logger.error("Invalid request parameter", exception);
        return exception.getMessage();
    }


}