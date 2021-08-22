package com.iyzico.challenge.helper.mapper;

import java.util.*;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.model.dto.ProductDto;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public ProductDto toProductDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setDescription(product.getDescription());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setStock(product.getStock());
        return productDto;
    }

    public Product toProduct(ProductDto productDto){
        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        return product;
    }

    public List<ProductDto> toProductDtos(List<Product> products){
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products){
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setDescription(product.getDescription());
            productDto.setName(product.getName());
            productDto.setPrice(product.getPrice());
            productDto.setStock(product.getStock());
            productDtos.add(productDto);
        }
        return productDtos;
    }
}
