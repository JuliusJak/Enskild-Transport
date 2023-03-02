package com.example.enskildtransport.model.exceptionHandler;

public class ExceptionHandler extends RuntimeException{
    public ExceptionHandler (Long id){
        super("Rout not found with id" + id);
    }
}
