package com.mysite.shoppingback.Repository;

import com.mysite.shoppingback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByUserId(String userId);

    Optional<User> findById(Long id);

    //Optional<User> findByUserId(String userId);


}
