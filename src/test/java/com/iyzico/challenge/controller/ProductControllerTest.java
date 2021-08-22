package com.iyzico.challenge.controller;

import com.iyzico.challenge.TestBaseUtil;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.service.product.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest extends TestBaseUtil {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

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
    public void addProduct_methodExecute_shouldCreateProduct() {
        ProductDto productDto = createDummyProductDto();
        when(productService.save(productDto)).thenReturn(productDto);

        ResponseEntity<ProductDto> productResponseEntity = productController.addProduct(productDto);

        Assert.assertNotNull(productResponseEntity);
        Assert.assertNotNull(productResponseEntity.getBody());
        Assert.assertEquals(HttpStatus.CREATED, productResponseEntity.getStatusCode());

        Assert.assertEquals(productDto.getName(), productResponseEntity.getBody().getName());
    }

    @Test
    public void getProduct_methodExecute_shouldGetProductBuyId() {
        ProductDto productDto = createDummyProductDtoWithId();
        when(productService.getById(new Long(1))).thenReturn(productDto);

        ResponseEntity<ProductDto> productResponseEntity = productController.getProduct(new Long(1));

        Assert.assertNotNull(productResponseEntity);
        Assert.assertNotNull(productResponseEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, productResponseEntity.getStatusCode());

        Assert.assertEquals(1L, productResponseEntity.getBody().getId().longValue());
    }

    @Test
    public void getProduct_methodExecute_shouldGetAllProducts() {
        ProductDto productOne = createDummyProductDto();
        ProductDto productTwo = createDummyProductDto();

        List<ProductDto> productList = new ArrayList<>();
        productList.add(productOne);
        productList.add(productTwo);

        when(productService.getAll()).thenReturn(productList);

        ResponseEntity<List<ProductDto>> productListResponse = productController.getProducts();

        Assert.assertNotNull(productListResponse);
        Assert.assertEquals(2, productListResponse.getBody().size());
    }

    @Test
    public void update_methodExecute_shouldUpdateProduct() {
        ProductDto productDtoOne = createDummyProductDtoWithId();
        ProductDto productDtoTwo = createDummyProductDtoWithId();
        productDtoTwo.setDescription("UPDATED");

        when(productService.update(new Long(1), productDtoOne)).thenReturn(productDtoTwo);

        ResponseEntity<ProductDto> productResponseEntity = productController.updateProduct(new Long(1), productDtoOne);

        Assert.assertNotNull(productResponseEntity);
        Assert.assertNotNull(productResponseEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, productResponseEntity.getStatusCode());

        Assert.assertNotEquals(productDtoOne.getDescription(), productDtoTwo.getDescription());
    }

    @Test
    public void deleteProduct_methodExecute_shouldDeleteProduct() {
        doNothing().when(productService).remove(new Long(1));

        ResponseEntity productResponseEntity = productController.deleteProduct(1L);

        Assert.assertNotNull(productResponseEntity);
        Assert.assertNull(productResponseEntity.getBody());
        Assert.assertEquals(HttpStatus.NO_CONTENT, productResponseEntity.getStatusCode());
    }
}
