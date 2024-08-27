package com.mysite.shoppingback.Service;

import com.mysite.shoppingback.Repository.CartItemRepository;
import com.mysite.shoppingback.Repository.CartRepository;
import com.mysite.shoppingback.Repository.ProductRepository;
import com.mysite.shoppingback.Repository.UserRepository;
import com.mysite.shoppingback.domain.Cart;
import com.mysite.shoppingback.domain.CartItem;
import com.mysite.shoppingback.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    public Cart getCartForUser(Long userId) {
        return cartRepository.findByUser_Id(userId);
    }

    public void addProductToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userService.findById(userId));
                    return cartRepository.save(newCart);
                });

        Product product = productService.getProductById(productId);
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setCart(cart);
                    newCartItem.setProduct(product);
                    return newCartItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);

        cart.setPrice(cart.getPrice() + (product.getPrice() * quantity));
        cartRepository.save(cart);
    }

    // 장바구니에 상품 추가 또는 수량 업데이트
    public void addOrUpdateProductInCart(Long userId, Long productId, int quantity) {
        Cart cart = getCartForUser(userId);
        if (cart == null) {
            throw new RuntimeException("Cart not found for member ID: " + userId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            // 이미 장바구니에 상품이 존재하면 수량 업데이트
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // 장바구니에 상품이 없는 경우 새로 추가
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cart.addCartItem(cartItem);
        }

        // 장바구니 총 가격 및 상품 수량 업데이트
        updateCartSummary(cart);

        cartRepository.save(cart);
    }

    private void updateCartSummary(Cart cart) {
        int totalCount = cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        cart.setCount(totalCount);
        cart.setPrice(totalPrice);
    }

    public void removeProductFromCart(Long userId, Long cartItemId) {
        Cart cart = getCartForUser(userId);

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Cart item not found"));

        cart.removeCartItem(cartItem);
        cartRepository.save(cart);


    }

    public void removeCartItem(Long itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemRepository.delete(cartItem);
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUser_Id(userId);
    }

    public int getCartItemsCount(Long userId) {
        Cart cart = getCartByUserId(userId);
        return (cart != null) ? cart.getCount() : 0;
    }

    //장바구니 비우기 메서드
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    public void clearCart(Cart cart) {
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
