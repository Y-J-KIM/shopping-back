package com.mysite.shoppingback.Repository;

import com.mysite.shoppingback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByUserId(String userId);


}
