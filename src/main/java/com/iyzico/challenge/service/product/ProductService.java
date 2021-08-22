package com.iyzico.challenge.service.product;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.model.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto getById(Long id);

    ProductDto save(ProductDto productDto);

    ProductDto update(Long id, ProductDto productDto);

    void remove(Long id);

    List<ProductDto> getAll();
}
