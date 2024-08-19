package com.mysite.shoppingback.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private Long id;    //번호
    private String name; //상품명
    private String description; //상세설명
    private Double price; //가격
    private String image;
    private int stock; //재고
    private boolean inSoldout; // 판매중/품절
}
