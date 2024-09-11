package com.nyanmyohtet.ecommerceservice.api.rest;

import com.nyanmyohtet.ecommerceservice.dto.AddProductToCartDto;
import com.nyanmyohtet.ecommerceservice.model.Cart;
import com.nyanmyohtet.ecommerceservice.model.CartItem;
import com.nyanmyohtet.ecommerceservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/cart-management/cart")
public class CartRestController {

    private final CartService cartService;

    // Get the cart for a specific user
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    // Add a product to the cart for a specific user
    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable Long userId,
            @RequestBody AddProductToCartDto addProductToCartDto) {
        Cart addedCart = cartService.addProductToCart(userId, addProductToCartDto.getProductId(), addProductToCartDto.getQuantity());
        return ResponseEntity.ok(addedCart);
    }

    // Update the quantity of a product in the cart

    // Remove a product from the cart
    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    // Clear the cart for a user
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
