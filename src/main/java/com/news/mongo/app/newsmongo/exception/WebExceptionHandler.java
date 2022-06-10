package com.news.mongo.app.newsmongo.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {




    /*@ExceptionHandler(NewsNotFoundException.class)
    protected ResponseEntity<ApiMessage> handleConflict(NewsNotFoundException ex) {
        log.error("Exception occurred, ", ex);
        ApiMessage apiMessage = new ApiMessage();
        ApiInfo apiInfo = new ApiInfo();
        ApiError apiError = new ApiError();
        *//*apiError.setCode(ex.getCode());
        apiError.setDescription(ex.getDescription());*//*
        apiError.setReason(ex.getMessage());
        apiInfo.setErrors(Collections.singletonList(apiError));
        apiInfo.setSuccess(false);
        apiInfo.setStatus("FAILED");
        apiMessage.setInfo(apiInfo);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiMessage);
    }*/
}
