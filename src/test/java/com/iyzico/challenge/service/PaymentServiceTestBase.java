package com.iyzico.challenge.service;

import com.iyzico.challenge.TestBaseUtil;
import com.iyzico.challenge.exception.PaymentException;
import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.model.request.PaymentRequest;
import com.iyzico.challenge.service.payment.PaymentService;
import com.iyzico.challenge.service.product.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
public class PaymentServiceTestBase extends TestBaseUtil {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProductService productService;

    @Test
    public void buyProduct_methodExecute_shouldBuyProduct() {
        ProductDto productDto = createDummyProductDto(10);
        ProductDto addedProduct = productService.save(productDto);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setProductId(addedProduct.getId());
        paymentRequest.setQuantity(4);
        paymentService.buyProduct(paymentRequest);

        ProductDto foundProduct = productService.getById(addedProduct.getId());
        Assert.assertEquals(6, foundProduct.getStock().intValue());
    }

    @Test
    public void buyProduct_productNotInStock_shouldNotBuyProduct() {
        ProductDto productDto = createDummyProductDto(10);
        ProductDto addedProduct = productService.save(productDto);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setProductId(addedProduct.getId());
        paymentRequest.setQuantity(15);

        try{
            paymentService.buyProduct(paymentRequest);
        }
        catch (PaymentException ex){
            Assert.assertEquals(ex.getMessage(), "There is not enough stock for the product");
        }
    }

    @Test
    public void buyProduct_quantityMoreThanStockCount_shouldNotBuyProduct() {
        ProductDto productDto = createDummyProductDto(10);
        ProductDto addedProduct = productService.save(productDto);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setProductId(addedProduct.getId());
        paymentRequest.setQuantity(-5);

        try{
            paymentService.buyProduct(paymentRequest);
        }
        catch (InvalidParameterException ex){
            Assert.assertEquals(ex.getMessage(), "Quantity can not be less than 0!");
        }
    }

    @Test
    public void buyProduct_manyClientsTryToGetSameProduct_shouldNotBuyProduct() {
        ProductDto productDto = createDummyProductDto(10);
        ProductDto addedProduct = productService.save(productDto);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setProductId(addedProduct.getId());
        paymentRequest.setQuantity(4);

        List<CompletableFuture> futures = new ArrayList<>();

        try{
            for (int i = 0; i < 3; i++) {
                CompletableFuture<String> future = paymentService.buyProduct(paymentRequest);
                futures.add(future);
            }
            futures.stream().forEach(f -> CompletableFuture.allOf(f).join());
        }
        catch (PaymentException ex){
            //Third client get this result
            Assert.assertEquals(ex.getMessage(), "There is not enough stock for the product");
        }
    }
}
