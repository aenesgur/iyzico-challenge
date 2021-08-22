package com.iyzico.challenge.service.payment;

import com.iyzico.challenge.model.request.PaymentRequest;

import java.util.concurrent.CompletableFuture;


public interface PaymentService {

    CompletableFuture<String> buyProduct(PaymentRequest paymentRequest);
}
