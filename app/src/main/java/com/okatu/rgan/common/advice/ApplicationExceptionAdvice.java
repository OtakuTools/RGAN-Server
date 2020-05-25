package com.okatu.rgan.common.advice;

import com.okatu.rgan.common.exception.*;
import com.okatu.rgan.common.model.GeneralErrorResult;
import com.okatu.rgan.common.model.MethodArgumentNotValidErrorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ApplicationExceptionAdvice {

    private static Logger logger = LoggerFactory.getLogger(ApplicationExceptionAdvice.class);

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    GeneralErrorResult entityNotFoundHandler(ResourceNotFoundException exception){
        logger.error("entity not found in repository", exception);
        return new GeneralErrorResult(exception.getMessage());
    }

    // HttpMessageNotReadableException是@RequestBody反序列化过程中抛出JsonParseException的包装，
    // 比方说JSON体多个逗号少个逗号
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    GeneralErrorResult InvalidInputParameterHandler(HttpMessageNotReadableException exception){
        logger.error("Invalid request parameter", exception);
        return new GeneralErrorResult(exception.getMessage());
    }

    // MethodArgumentNotValidException是违反了请求参数体中的@Valid注解约束而抛出的
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    MethodArgumentNotValidErrorResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        logger.error("Invalid request parameter", exception);
        MethodArgumentNotValidErrorResult errorResult = new MethodArgumentNotValidErrorResult();
        errorResult.setMessage("Invalid request parameter");

        for(FieldError fieldError : exception.getBindingResult().getFieldErrors()){
            errorResult.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorResult;
    }

    @ResponseBody
    @ExceptionHandler({ResourceAccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    GeneralErrorResult resourceForbiddenExceptionHandler(ResourceAccessDeniedException exception){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            logger.error("anonymous user try to access authenticated resource", exception);
        }else{
            logger.error("user: {} try to access authenticated resource", authentication.getName(), exception);
        }
        return new GeneralErrorResult(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    GeneralErrorResult constraintViolationExceptionHandler(ConstraintViolationException exception){
        logger.error("Backup controller exception handler", exception);
        return new GeneralErrorResult("Something wrong happened inside the system, please retry later");
    }

    @ResponseBody
    @ExceptionHandler({VerificationEmailUnavailableException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    GeneralErrorResult verificationEmailUnavailableExceptionHandler(VerificationEmailUnavailableException exception){
        logger.error(exception.getMessage(), exception);
        return new GeneralErrorResult(exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({UniquenessViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    GeneralErrorResult uniquenessViolationExceptionHandler(UniquenessViolationException exception){
        logger.error(exception.getMessage(), exception);
        return new GeneralErrorResult(exception.getMessage());
    }

    // missing RequestParam
    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    GeneralErrorResult missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException exception){
        logger.error("Parameter of type {}, name {} is missing", exception.getParameterType(), exception.getParameterName(), exception);
        return new GeneralErrorResult("parameter " + exception.getParameterName() + " is missing");
    }

    @ResponseBody
    @ExceptionHandler({ResponseStatusException.class})
    ResponseEntity<?> responseStatusExceptionHandler(ResponseStatusException exception){
        logger.error("Response status exception", exception);
        GeneralErrorResult generalErrorResult = new GeneralErrorResult(exception.getMessage());
        return new ResponseEntity<>(generalErrorResult, exception.getStatus());
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    GeneralErrorResult backupExceptionHandler(Exception exception){
        logger.error("Backup exception handler", exception);
        return new GeneralErrorResult("Something wrong happened inside the system, please retry later");
    }

//    @ResponseBody
//    @ExceptionHandler({ResponseStatusException.class})
//    ResponseEntity<String> responseStatusExceptionHandler(ResponseStatusException exception){
//        logger.error("Response status exception", exception);
//        return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
//    }
}