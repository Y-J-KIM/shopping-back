package com.mysite.shoppingback.Repository;

import com.mysite.shoppingback.domain.Cart;
import com.mysite.shoppingback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
    Cart findByUser_Id(Long userId);
}
