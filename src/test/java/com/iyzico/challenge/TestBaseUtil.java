package com.iyzico.challenge;

import com.iyzico.challenge.model.dto.ProductDto;

import java.math.BigDecimal;

public class TestBaseUtil {

    protected ProductDto createDummyProductDto(){
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Name");
        productDto.setPrice(BigDecimal.TEN);
        productDto.setDescription("Test Description");
        productDto.setStock(10);
        return productDto;
    }
    protected ProductDto createDummyProductDto(Integer stock){
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Name");
        productDto.setPrice(BigDecimal.TEN);
        productDto.setDescription("Test Description");
        productDto.setStock(stock);
        return productDto;
    }

}
