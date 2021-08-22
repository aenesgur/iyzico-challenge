package com.iyzico.challenge.model.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PaymentRequest {

    @NotNull
    private Long productId;

    @NotNull
    @Min(value = 1,message = "Quantity can not be less than 1")
    private int quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
