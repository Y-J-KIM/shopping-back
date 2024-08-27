package com.mysite.shoppingback.DTO;

import com.mysite.shoppingback.domain.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;

    public CartDTO(Long id, Long userId, List<CartItemDTO> items) {
        this.id = id;
        this.userId = userId;
        this.items = items;
    }

    public CartDTO() {
        
    }

    public CartDTO(Long userId, List<CartItemDTO> cartItems) {
    }
}
