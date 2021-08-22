package com.iyzico.challenge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.TestBaseUtil;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.model.dto.ProductDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration()
@SpringBootTest
public class ProductControllerIntegrationTest extends TestBaseUtil {

    @Value("${application.path.product}")
    private String productApiPath;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mock;

    private ProductDto savedProduct;

    @Test
    public void postProduct_apiExecute_shouldCreateProduct() throws Exception {

        ProductDto productDto = createDummyProductDto();

        String resultJson = mock.perform(post(productApiPath)
                .content(objectMapper.writeValueAsBytes(productDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(productDto.getName()))
                .andExpect(jsonPath("$.description").value(productDto.getDescription()))
                .andExpect(jsonPath("$.price").value(productDto.getPrice()))
                .andExpect(jsonPath("$.stock").value(productDto.getStock()))
                .andReturn().getResponse().getContentAsString();

        savedProduct = objectMapper.readValue(resultJson, ProductDto.class);
    }

    @Test
    public void getProductById_apiExecute_shouldGetProductById() throws Exception {

        ProductDto productDto = createDummyProductDto();

        String resultJson = mock.perform(post(productApiPath)
                .content(objectMapper.writeValueAsBytes(productDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        savedProduct = objectMapper.readValue(resultJson, ProductDto.class);
        Assert.assertNotNull(savedProduct);

        mock.perform(get(productApiPath + "/" + savedProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(savedProduct.getId()))
                .andExpect(jsonPath("$.name").value(savedProduct.getName()))
                .andExpect(jsonPath("$.description").value(savedProduct.getDescription()))
                .andExpect(jsonPath("$.stock").value(savedProduct.getStock()));
    }

    @Test
    public void deleteProduct_apiExecute_shouldDeleteProduct() throws Exception {

        ProductDto productDto = createDummyProductDto();

        String resultJson = mock.perform(post(productApiPath)
                .content(objectMapper.writeValueAsBytes(productDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        savedProduct = objectMapper.readValue(resultJson, ProductDto.class);
        Assert.assertNotNull(savedProduct);

        mock.perform(delete(productApiPath + "/" + savedProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
