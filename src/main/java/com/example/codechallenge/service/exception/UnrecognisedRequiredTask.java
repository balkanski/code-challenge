package com.example.codechallenge.service.exception;

public class UnrecognisedRequiredTask extends RuntimeException {

    public UnrecognisedRequiredTask(String message){
        super(message);
    }
}
