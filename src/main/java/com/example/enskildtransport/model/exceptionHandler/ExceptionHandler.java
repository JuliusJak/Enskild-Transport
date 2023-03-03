package com.example.enskildtransport.model.exceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;

public class ExceptionHandler extends RuntimeException{
    public ExceptionHandler (String message){
        super(message);
    }
}
