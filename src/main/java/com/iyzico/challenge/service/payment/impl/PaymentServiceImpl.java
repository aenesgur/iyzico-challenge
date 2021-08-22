package com.iyzico.challenge.service.payment.impl;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.PaymentException;
import com.iyzico.challenge.model.request.PaymentRequest;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.IyzicoPaymentService;
import com.iyzico.challenge.service.PaymentServiceClients;
import com.iyzico.challenge.service.payment.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IyzicoPaymentService iyzicoPaymentService;

    @Autowired
    private PaymentServiceClients paymentServiceClients;

    @Override
    public CompletableFuture<String> buyProduct(PaymentRequest paymentRequest) {

        Product product = productRepository.findById(paymentRequest.getProductId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"No product found with id: ".concat(String.valueOf(paymentRequest.getProductId()))));

        BigDecimal price = product.getPrice().multiply(new BigDecimal(paymentRequest.getQuantity()));

        updateStock(product, paymentRequest.getQuantity());
        try{
             return paymentServiceClients.call(price);
        }
        catch (Exception ex){
            logger.error("Error encountered while payment", ex);
            throw new PaymentException("Something went wrong while paying");
        }
    }

    /*
        While the stock information is being updated,
        the synchronized keyword is used in the method that performs this operation.
        When the Synchronized keyword is placed at the beginning of the method,
        the threads that want to access the method enter the method in order,
        and the other thread cannot enter before one thread finishes the method.
        In this way, customers who pay for the same product at the same time are prevented
        from purchasing the product if the stock runs out.
     */
    private synchronized void updateStock(Product product, int quantity){
        if (product.getStock() >= quantity){
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        } else{
            logger.info("There is not enough stock for the product");
            throw new PaymentException("There is not enough stock for the product");
        }
    }
}
