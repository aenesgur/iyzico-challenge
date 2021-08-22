package com.iyzico.challenge.controller;

import com.iyzico.challenge.TestBaseUtil;
import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.model.request.PaymentRequest;
import com.iyzico.challenge.service.payment.PaymentService;
import com.iyzico.challenge.service.product.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = PaymentController.class)
public class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private ProductDto createDummyProductDtoWithId() {
        ProductDto productDto = new ProductDto();
        productDto.setId(new Long(1));
        productDto.setName("Test Name");
        productDto.setPrice(BigDecimal.TEN);
        productDto.setDescription("Test Description");
        productDto.setStock(10);
        return productDto;
    }

    @Test
    public void paymentProduct_methodExecute_shouldBuyProduct() {
        ProductDto productDto = createDummyProductDtoWithId();
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setProductId(productDto.getId());
        paymentRequest.setQuantity(3);

        when(productService.getById(new Long(1))).thenReturn(productDto);

        when(paymentService.buyProduct(paymentRequest)).thenReturn(CompletableFuture.completedFuture("completed"));

        paymentController.paymentProduct(paymentRequest);;
    }

}
