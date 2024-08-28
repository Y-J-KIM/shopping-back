package com.mysite.shoppingback.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;  // 고유한 유저 식별자

    private String username;
    private String password;
    private String email;
    private String address;

    // 사용자와 장바구니 사이의 관계를 설정합니다.
    @OneToOne(mappedBy = "user")
    private Cart cart;

    // 역할 필드
    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

}
