package com.mysite.shoppingback.Service;

import com.mysite.shoppingback.Repository.CartItemRepository;
import com.mysite.shoppingback.Repository.CartRepository;
import com.mysite.shoppingback.Repository.ProductRepository;
import com.mysite.shoppingback.Repository.UserRepository;
import com.mysite.shoppingback.domain.Cart;
import com.mysite.shoppingback.domain.CartItem;
import com.mysite.shoppingback.domain.Product;
import com.mysite.shoppingback.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository; // User 엔티티 조회를 위한 레포지토리

    // 사용자 ID로 장바구니를 조회
    public Cart getCartByUserId(String userId) {
        User user = userRepository.findByUserId(userId); // userId를 통해 User 객체 조회
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return cartRepository.findByUser(user); // User 객체를 통해 Cart 조회
    }

    // 장바구니에 아이템 추가
    public void addItemToCart(String userId, Long productId, int quantity) {
        try {
            Cart cart = cartRepository.findByUserId(userId);
            if (cart == null) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
                cart = new Cart();
                cart.setUser(user);
                cartRepository.save(cart);
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

            CartItem existingCartItem = cartItemRepository.findByProductIdAndCartId(productId, cart.getId());

            if (existingCartItem != null) {
                existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                cartItemRepository.save(existingCartItem);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setCart(cart);
                cartItemRepository.save(cartItem);
            }

            cart.calculateTotalPrice(); // 총 가격 계산
            cartRepository.save(cart); // 장바구니 저장
        } catch (Exception e) {
            e.printStackTrace(); // 로그에 오류 출력
            throw new RuntimeException("Failed to add item to cart", e);
        }
    }


    // 사용자 ID로 장바구니를 가져오는 메서드
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUser_Id(userId);
    }




    // 장바구니 아이템 수량 업데이트
    public void updateCartItemQuantity(String userId, Long itemId, int quantity) {
        Cart cart = getCartByUserId(userId); // 유저에 맞는 장바구니 조회
        if (cart != null) {
            CartItem item = cartItemRepository.findByIdAndCartId(itemId, cart.getId());
            if (item != null) {
                item.setQuantity(quantity);
                cartItemRepository.save(item); // CartItem 업데이트
                cart.calculateTotalPrice(); // 총 가격 재계산
                cartRepository.save(cart); // Cart 업데이트
            }
        }
    }

    // 장바구니에서 아이템 제거
    public void removeCartItem(String userId, Long itemId) {
        Cart cart = getCartByUserId(userId); // 유저에 맞는 장바구니 조회
        if (cart != null) {
            CartItem item = cartItemRepository.findByIdAndCartId(itemId, cart.getId());
            if (item != null) {
                cart.removeCartItem(item); // Cart에서 아이템 제거
                cartItemRepository.delete(item); // CartItem 삭제
                cart.calculateTotalPrice(); // 총 가격 재계산
                cartRepository.save(cart); // Cart 업데이트
            }
        }
    }

    // 비로그인 상태에서 세션 데이터 동기화
    public void syncCart(String userId) {
        // 세션에 저장된 데이터를 사용자의 장바구니와 동기화하는 로직 작성
    }

    // 로그인 후 장바구니 로드
    public Cart loadCartAfterLogin(String userId) {
        return getCartByUserId(userId);
    }


}
