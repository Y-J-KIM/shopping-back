package com.mysite.shoppingback.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {

    private Long productId;
    private String productName;
    private int quantity;
    private double price;

    public CartItemDTO(Long productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

}
