package com.iyzico.challenge.controller;

import com.iyzico.challenge.model.request.PaymentRequest;
import com.iyzico.challenge.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity paymentProduct(@Valid @RequestBody PaymentRequest paymentRequest){
        paymentService.buyProduct(paymentRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
