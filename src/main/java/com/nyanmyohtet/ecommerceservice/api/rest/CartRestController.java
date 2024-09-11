package com.nyanmyohtet.ecommerceservice.api.rest;

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
    public ResponseEntity<CartItem> addProductToCart(
            @PathVariable Long userId,
            @RequestBody CartItem cartItem) {
        CartItem addedCartItem = cartService.addProductToCart(userId, cartItem);
        return ResponseEntity.ok(addedCartItem);
    }

    // Update the quantity of a product in the cart
    @PutMapping("/{userId}/update")
    public ResponseEntity<CartItem> updateProductInCart(
            @PathVariable Long userId,
            @RequestBody CartItem updatedCartItem) {
        CartItem cartItem = cartService.updateProductInCart(userId, updatedCartItem);
        return ResponseEntity.ok(cartItem);
    }

    // Remove a product from the cart
    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Void> removeProductFromCart(
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
