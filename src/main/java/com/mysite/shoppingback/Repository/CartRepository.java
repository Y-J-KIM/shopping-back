package com.mysite.shoppingback.Repository;

import com.mysite.shoppingback.domain.Cart;
import com.mysite.shoppingback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
