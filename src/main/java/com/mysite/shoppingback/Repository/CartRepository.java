package com.mysite.shoppingback.Repository;

import com.mysite.shoppingback.domain.Cart;
import com.mysite.shoppingback.domain.CartItem;
import com.mysite.shoppingback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUser(User user);  // User로 Cart를 조회하는 메서드
    void deleteByUser(User user); // User로 Cart를 삭제하는 메서드

    Cart findByUser_Id(Long userId);

    Cart findByUserId(Long userId);
}
