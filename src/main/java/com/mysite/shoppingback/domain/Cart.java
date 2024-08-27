package com.mysite.shoppingback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//@Entity
//@Getter
//@Setter
//public class Cart {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
//    private List<CartItem> items;
//}

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public List<CartItem> getCartItems() {
        return cart_items;
    }

    @OneToMany(mappedBy = "cart")
    @JsonIgnore
    private List<CartItem> cart_items = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user; //구매자

    private int count; //총 상품 갯수

    @Column(nullable = false)
    private double price; //가격

    // 카트에서 총 가격 계산 메서드
    public double calculateTotalPrice() {
        this.price = this.cart_items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        return 0;
    }

    // 카트에 아이템 추가 메서드
    public void addCartItem(CartItem cartItem) {
        cart_items.add(cartItem);
        cartItem.setCart(this);

    }

    // 카트에서 아이템 제거 메서드
    public void removeCartItem(CartItem cartItem) {
        cart_items.remove(cartItem);
        cartItem.setCart(null);

    }

    //카트 전체 수량
    public int getTotalQuantity() {
        return cart_items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
