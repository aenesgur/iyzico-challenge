package com.iyzico.challenge.service;

import com.iyzico.challenge.TestBaseUtil;
import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.service.product.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
public class ProductServiceTestBase extends TestBaseUtil {

    @Autowired
    private ProductService productService;

    @Test
    public void save_methotExecute_shouldAddProduct(){
        ProductDto productDto = createDummyProductDto();

        ProductDto addedProduct = productService.save(productDto);

        Assert.assertNotNull(addedProduct);
        Assert.assertTrue(addedProduct.getId() != 0);
        Assert.assertEquals(productDto.getName(), addedProduct.getName());
        Assert.assertEquals(productDto.getDescription(), addedProduct.getDescription());
    }

    @Test
    public void update_methodExecute_shouldUpdateProduct() {
        ProductDto productDto = createDummyProductDto();

        ProductDto addedProduct = productService.save(productDto);

        addedProduct.setName(addedProduct.getName() + "Update Unit Test For Name Field");
        addedProduct.setDescription(addedProduct.getDescription() + "Update Unit Test For Name Description Field");
        addedProduct.setPrice(addedProduct.getPrice().add(new BigDecimal(55)));
        addedProduct.setStock(addedProduct.getStock() + 55);

        ProductDto updatedProductDto = productService.update(addedProduct.getId(),addedProduct);

        Assert.assertNotNull(updatedProductDto);

        Assert.assertEquals(addedProduct.getId(), updatedProductDto.getId());
        Assert.assertEquals(addedProduct.getName(), updatedProductDto.getName());
        Assert.assertEquals(addedProduct.getDescription(), updatedProductDto.getDescription());
    }

    @Test
    public void getById_methodExecute_shouldGetProductById() {
        ProductDto productDto = createDummyProductDto();

        ProductDto addedProduct = productService.save(productDto);
        Assert.assertNotNull(addedProduct);

        ProductDto foundProduct = productService.getById(addedProduct.getId());
        Assert.assertNotNull(foundProduct);
        Assert.assertEquals(addedProduct.getId(), foundProduct.getId());
        Assert.assertEquals(addedProduct.getName(), foundProduct.getName());
        Assert.assertEquals(addedProduct.getDescription(), foundProduct.getDescription());
    }

    @Test
    public void getById_idNotContainsInDb_shouldNotGetProductById() {
        ProductDto productDto = createDummyProductDto();

        ProductDto addedProduct = productService.save(productDto);
        Assert.assertNotNull(addedProduct);

        try{
            productService.getById(new Long(3));
        }
        catch (ResponseStatusException ex){
            Assert.assertEquals(ex.getReason(), "No product found with id: 3");
        }
    }

    @Test
    public void delete_methodExecute_shouldDeleteProduct() {
        ProductDto productDto = createDummyProductDto();
        ProductDto addedProduct = productService.save(productDto);

        productService.remove(addedProduct.getId());

        try{
            productService.getById(addedProduct.getId());
        }
        catch (ResponseStatusException ex){
            Assert.assertEquals(ex.getReason(), "No product found with id: "+addedProduct.getId());
        }
    }

    @Test
    public void getAll_methodExecute_shouldListAllProducts() {
        ProductDto productDtoOne = createDummyProductDto();
        ProductDto addedProductOne = productService.save(productDtoOne);

        ProductDto productDtoTwo = createDummyProductDto();
        ProductDto addedProductTwo = productService.save(productDtoTwo);

        List<ProductDto> productDtos = productService.getAll();

        Assert.assertTrue(productDtos.size()>0);
    }
}
