package com.example.softwell.controller;

import com.example.softwell.exception.CooldownException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CooldownException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) //
    @ResponseBody
    public String handleCooldownException(CooldownException ex) {
        return ex.getMessage();
    }
}