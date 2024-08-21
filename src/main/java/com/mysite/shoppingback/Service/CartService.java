package com.mysite.shoppingback.Service;

import com.mysite.shoppingback.DTO.CartDTO;
import com.mysite.shoppingback.DTO.CartItemDTO;
import com.mysite.shoppingback.Repository.CartItemRepository;
import com.mysite.shoppingback.Repository.CartRepository;
import com.mysite.shoppingback.domain.Cart;
import com.mysite.shoppingback.domain.CartItem;
import com.mysite.shoppingback.domain.Product;
import com.mysite.shoppingback.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductService productService; // Assume you have a service to fetch product details
    @Autowired
    private UserService userService; // Assume you have a service to fetch user details

    // 사용자에 대한 장바구니 조회
    public CartDTO getCartForUser(Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            // 장바구니가 존재하지 않는 경우 새 장바구니를 생성
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }
        return convertToDTO(cart);
    }

    // 장바구니에 항목 추가
    public CartDTO addItemToCart(Long userId, Long productId, int quantity) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productService.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        // 장바구니에서 기존 항목을 찾기
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        if (existingItem.isPresent()) {
            // 기존 항목이 있으면 수량 업데이트
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // 새로운 항목 추가
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }

        return convertToDTO(cart);
    }

    // 장바구니 항목 삭제
    public CartDTO removeItemFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        Cart cart = cartItem.getCart();
        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return convertToDTO(cart);
    }

    // 장바구니 항목 수량 업데이트
    public CartDTO updateItemQuantity(Long cartItemId, int newQuantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        if (newQuantity <= 0) {
            // 수량이 0 이하인 경우 항목 삭제
            return removeItemFromCart(cartItemId);
        } else {
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }

        return convertToDTO(cartItem.getCart());
    }

    // Cart 엔티티를 DTO로 변환하는 메서드
    private CartDTO convertToDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUser().getId());
        cartDTO.setItems(cart.getItems().stream()
                .map(this::convertToDTO)
                .toList());
        return cartDTO;
    }

    // CartItem 엔티티를 DTO로 변환하는 메서드
    private CartItemDTO convertToDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setUserId(cartItem.getId());
        cartItemDTO.setProductId(cartItem.getProduct().getId());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        return cartItemDTO;
    }
}
