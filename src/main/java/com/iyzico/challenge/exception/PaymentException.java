package com.iyzico.challenge.exception;

public class PaymentException extends RuntimeException {
    public PaymentException(String errorMessage){
        super(errorMessage);
    }
}
