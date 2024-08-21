package com.mysite.shoppingback.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Long userId;
    private Long productId;
    private int quantity;
}
