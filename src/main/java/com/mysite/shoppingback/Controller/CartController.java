package com.mysite.shoppingback.Controller;

import com.mysite.shoppingback.DTO.CartItemDTO;
import com.mysite.shoppingback.Repository.UserRepository;
import com.mysite.shoppingback.Service.CartService;
import com.mysite.shoppingback.Service.UserService;
import com.mysite.shoppingback.domain.Cart;
import com.mysite.shoppingback.domain.CartItem;
import com.mysite.shoppingback.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    // 사용자 ID로 장바구니 조회
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<?> addItemToCart(
            HttpSession session,
            @PathVariable("userId") Long userId,  // 사용자 엔티티의 id
            @RequestBody CartItem item) {
        try {
            // 세션에서 사용자 ID 확인
            Long sessionUserId = (Long) session.getAttribute("userId");  // 세션에 저장된 사용자 ID
            System.out.println("sessionUserId: " + sessionUserId);
            System.out.println("userId: " + userId);
            if (sessionUserId == null || !sessionUserId.equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session or user ID");
            }

            // CartItem 유효성 검증
            if (item == null || item.getProduct() == null || item.getProduct().getId() == null || item.getQuantity() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid CartItem: product or quantity is missing");
            }


            // 새로운 메서드 호출
            cartService.addItemToCart(userId, item);
            return ResponseEntity.ok("Item added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add item to cart: " + e.getMessage());
        }
    }

    // 장바구니 아이템 수량 업데이트
    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Void> updateCartItemQuantity(@PathVariable Long userId, @PathVariable Long itemId, @RequestParam int quantity) {
        cartService.updateCartItemQuantity(userId, itemId, quantity);
        return ResponseEntity.ok().build();
    }

    // 장바구니에서 아이템 제거
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long userId, @PathVariable Long itemId) {
        cartService.removeCartItem(userId, itemId);
        return ResponseEntity.ok().build();
    }

    // 비로그인 상태에서 세션 데이터 동기화 (추가적인 구현 필요)
    @PostMapping("/{userId}/sync")
    public ResponseEntity<Void> syncCart(@PathVariable Long userId) {
        cartService.syncCart(userId);
        return ResponseEntity.ok().build();
    }

    // 로그인 후 장바구니 로드
    @GetMapping("/{userId}/load")
    public ResponseEntity<Cart> loadCartAfterLogin(@PathVariable Long userId) {
        Cart cart = cartService.loadCartAfterLogin(userId);
        return ResponseEntity.ok(cart);
    }
}
