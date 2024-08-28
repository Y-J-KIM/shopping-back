//package com.mysite.shoppingback.Service;
//
//import com.mysite.shoppingback.Repository.CartItemRepository;
//import com.mysite.shoppingback.Repository.CartRepository;
//import com.mysite.shoppingback.Repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class CartServiceImpl extends CartService {
//
//    @Autowired
//    private final CartRepository cartRepository;
//
//    @Autowired
//    private final UserRepository userRepository;
//
//    private final CartItemRepository cartItemRepository;
//
//
//    public CartServiceImpl(CartRepository cartRepository, CartRepository cartRepository1, UserRepository userRepository, CartItemRepository cartItemRepository) {
//        super(cartRepository);
//        this.cartRepository = cartRepository1;
//        this.userRepository = userRepository;
//        this.cartItemRepository = cartItemRepository;
//    }
//}
