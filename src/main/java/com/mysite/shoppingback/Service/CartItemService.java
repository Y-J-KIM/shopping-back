package com.mysite.shoppingback.Service;

import com.mysite.shoppingback.Repository.CartItemRepository;
import com.mysite.shoppingback.domain.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id).orElse(null);
    }

    public void updateCartItemQuantity(Long cartItemId, int quantity) {
        // 장바구니 항목 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("장바구니 항목을 찾을 수 없습니다."));

        // 수량 업데이트
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);  // 변경된 항목 저장
    }

    public List<CartItem> getCartItemsByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }
}
