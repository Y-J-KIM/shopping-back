package com.mysite.shoppingback.Controller;

import com.mysite.shoppingback.DTO.CartDTO;
import com.mysite.shoppingback.DTO.CartItemDTO;  // CartItemDTO를 추가해야 합니다.
import com.mysite.shoppingback.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<CartDTO> getCart(@RequestParam("userId") Long userId) {
        CartDTO cart = cartService.getCartForUser(userId);
        return ResponseEntity.ok(cart);
    }

    // 장바구니에 항목 추가
    @PostMapping("/add")
    public ResponseEntity<CartDTO> addItemToCart(@RequestBody CartItemDTO cartItemDTO) {
        CartDTO cart = cartService.addItemToCart(cartItemDTO.getUserId(), cartItemDTO.getProductId(), cartItemDTO.getQuantity());
        return ResponseEntity.ok(cart);
    }

    // 장바구니 항목 삭제
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long cartItemId) {
        CartDTO cart = cartService.removeItemFromCart(cartItemId);
        return ResponseEntity.ok(cart);
    }

    // 장바구니 항목 수량 업데이트
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartDTO> updateItemQuantity(@PathVariable Long cartItemId,
                                                      @RequestBody CartItemDTO cartItemDTO) {
        CartDTO cart = cartService.updateItemQuantity(cartItemId, cartItemDTO.getQuantity());
        return ResponseEntity.ok(cart);
    }
}
