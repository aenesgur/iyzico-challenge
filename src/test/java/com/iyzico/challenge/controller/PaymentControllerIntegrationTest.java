package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.TestBaseUtil;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.model.request.PaymentRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ConfigurationProperties(prefix = "test.path")
@AutoConfigureMockMvc
@EnableAutoConfiguration()
@SpringBootTest
public class PaymentControllerIntegrationTest extends TestBaseUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${application.path.payment}")
    private String paymentApiPath;

    @Value("${application.path.product}")
    private String productApiPath;


    @Autowired
    private MockMvc mock;

    private ProductDto savedProduct;

    @Test
    public void postPayment_apiExecute_shouldBuyProduct() throws Exception {

        ProductDto productDto = createDummyProductDto();

        String resultJson = mock.perform(post(productApiPath)
                .content(objectMapper.writeValueAsBytes(productDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        savedProduct = objectMapper.readValue(resultJson, ProductDto.class);

        PaymentRequest request = new PaymentRequest();
        request.setProductId(savedProduct.getId());
        request.setQuantity(2);

        mock.perform(post(paymentApiPath)
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
