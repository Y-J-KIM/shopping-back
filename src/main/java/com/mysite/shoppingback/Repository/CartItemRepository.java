package com.mysite.shoppingback.Repository;

import com.mysite.shoppingback.domain.Cart;
import com.mysite.shoppingback.domain.CartItem;
import com.mysite.shoppingback.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//public interface CartItemRepository extends JpaRepository<CartItem, Long> {
//    CartItem findByCartAndProduct(Cart cart, Product product);
//}

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    @Query("SELECT c FROM CartItem c WHERE c.userId = :userId")
    List<CartItem> findByUserId(Long userId);
}
