package com.mysite.shoppingback.Repository;

import com.mysite.shoppingback.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
