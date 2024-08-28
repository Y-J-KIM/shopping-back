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
    // 장바구니 ID와 아이템 ID로 CartItem을 찾는 메서드
    CartItem findByIdAndCartId(Long itemId, Long cartId);


    CartItem findByProductIdAndCartId(Long productId, Long id);
}
