package com.iyzico.challenge.service.product.impl;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.helper.mapper.ProductMapper;
import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.IyzicoPaymentService;
import com.iyzico.challenge.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> {
            logger.error("No product found with id: ".concat(String.valueOf(id)));
            return new ResponseStatusException(HttpStatus.NOT_FOUND,"No product found with id: ".concat(String.valueOf(id)));
        });
        return productMapper.toProductDto(product);
    }

    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = productRepository.save(productMapper.toProduct(productDto));
        return productMapper.toProductDto(product);
    }

    @Override
    public ProductDto update(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id).orElseThrow(()-> {
            logger.error("No product found with id: ".concat(String.valueOf(id)));
            return new ResponseStatusException(HttpStatus.NOT_FOUND,"No product found with id: ".concat(String.valueOf(id)));
        });        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());

        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductDto(updatedProduct);
    }

    @Override
    public void remove(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> {
            logger.error("No product found with id: ".concat(String.valueOf(id)));
            return new ResponseStatusException(HttpStatus.NOT_FOUND,"No product found with id: ".concat(String.valueOf(id)));
        });
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getAll() {
        List<Product> products = productRepository.findAll();
        return productMapper.toProductDtos(products);
    }
}
