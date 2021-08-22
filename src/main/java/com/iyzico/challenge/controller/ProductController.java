package com.iyzico.challenge.controller;

import java.util.*;
import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/product")
public class ProductController
{
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(){
        List<ProductDto> productDtos = productService.getAll();
        return ResponseEntity.ok().body(productDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        ProductDto productDto = productService.getById(id);
        return ResponseEntity.ok().body(productDto);
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto addedProduct = productService.save(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.update(id, productDto);
        return ResponseEntity.ok().body(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        productService.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
