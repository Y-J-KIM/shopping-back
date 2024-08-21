package com.mysite.shoppingback.Repository;

import com.mysite.shoppingback.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
