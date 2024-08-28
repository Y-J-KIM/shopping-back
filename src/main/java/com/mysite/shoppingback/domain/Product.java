package com.mysite.shoppingback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //상품 이름

    private String description; //상세 설면

    @Column(nullable = false)
    private double price; //가격

    private int stock; //재고

    private boolean inSoldout; // 판매중/품절

    private String image; //상품 사진

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Product(Long productId) {
    }

//    @OneToMany(mappedBy = "product")
//    @JsonIgnore
//    private List<CartItem> cart_items = new ArrayList<>();
//

//
//    @OneToMany(mappedBy = "product")
//    @JsonIgnore
//    private Set<Review> reviews;


}

