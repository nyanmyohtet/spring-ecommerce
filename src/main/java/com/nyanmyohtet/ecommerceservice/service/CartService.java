package com.nyanmyohtet.ecommerceservice.service;

import com.nyanmyohtet.ecommerceservice.model.Cart;
import com.nyanmyohtet.ecommerceservice.model.CartItem;

public interface CartService {
    Cart getCartByUserId(Long userId);
    CartItem addProductToCart(Long userId, CartItem cartItem);
    CartItem updateProductInCart(Long userId, CartItem updatedCartItem);
    void removeProductFromCart(Long userId, Long productId);
    void clearCart(Long userId);
}
