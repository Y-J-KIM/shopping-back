package com.mysite.shoppingback.Controller;

import com.mysite.shoppingback.DTO.CartDTO;
import com.mysite.shoppingback.DTO.CartItemDTO;  // CartItemDTO를 추가해야 합니다.
import com.mysite.shoppingback.Service.CartItemService;
import com.mysite.shoppingback.Service.CartService;
import com.mysite.shoppingback.Service.ProductService;
import com.mysite.shoppingback.Service.UserService;
import com.mysite.shoppingback.domain.Cart;
import com.mysite.shoppingback.domain.Product;
import com.mysite.shoppingback.domain.User;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") int quantity,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Unauthorized"));
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Member not found"));
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Product not found"));
        }

        try {
            cartService.addProductToCart(user.getId(), productId, quantity);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Collections.singletonMap("message", "Product added to cart"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error adding product to cart"));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> updateCartItem(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") int newQuantity,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Unauthorized"));
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Member not found"));
        }

        Long userId = user.getId();
        cartService.addOrUpdateProductInCart(userId, productId, newQuantity);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap("message", "Cart item updated"));
    }

    @GetMapping("/items")
    public ResponseEntity<Cart> getCartItems(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Long userId = user.getId();
        Cart cart = cartService.getCartForUser(userId);
        if (cart == null) {
            return ResponseEntity.ok(new Cart());
        }

        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/remove/{itemId}")
    public ResponseEntity<Map<String, String>> removeCartItem(@PathVariable Long itemId) {
        try {
            cartService.removeCartItem(itemId);
            return ResponseEntity.ok(Collections.singletonMap("message", "Item removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error removing item"));
        }
    }

    @PostMapping("/updateQuantity")
    public ResponseEntity<Map<String, String>> updateQuantity(
            @RequestParam("id") Long id,
            @RequestParam("quantity") int quantity) {

        try {
            cartItemService.updateCartItemQuantity(id, quantity);
            return ResponseEntity.ok(Collections.singletonMap("message", "Quantity updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Error updating quantity"));
        }
    }
}
