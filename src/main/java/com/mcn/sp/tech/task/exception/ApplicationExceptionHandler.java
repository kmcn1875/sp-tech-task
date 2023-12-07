package com.mcn.sp.tech.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(value = {InvalidMeterReadingException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidMeterReadingException(InvalidMeterReadingException ex) {
        return ex.getMessage();
    }
}
