package com.nyanmyohtet.ecommerceservice.service;

import com.nyanmyohtet.ecommerceservice.model.Cart;
import com.nyanmyohtet.ecommerceservice.model.CartItem;

public interface CartService {
    Cart getCartByUserId(Long userId);
    Cart addProductToCart(Long userId, Long productId, int quantity);
    CartItem updateProductInCart(Long userId, CartItem updatedCartItem);
    Cart removeProductFromCart(Long userId, Long productId);
    void clearCart(Long userId);
}
