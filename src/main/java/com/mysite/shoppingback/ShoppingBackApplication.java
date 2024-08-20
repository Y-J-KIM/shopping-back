package com.mysite.shoppingback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //자동 등록,수정 시간 업데이트
public class ShoppingBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingBackApplication.class, args);
    }

}
