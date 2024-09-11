package com.nyanmyohtet.ecommerceservice.service.Impl;

import com.nyanmyohtet.ecommerceservice.exception.ResourceNotFoundException;
import com.nyanmyohtet.ecommerceservice.model.Cart;
import com.nyanmyohtet.ecommerceservice.model.CartItem;
import com.nyanmyohtet.ecommerceservice.model.Product;
import com.nyanmyohtet.ecommerceservice.model.User;
import com.nyanmyohtet.ecommerceservice.repository.CartItemRepository;
import com.nyanmyohtet.ecommerceservice.repository.CartRepository;
import com.nyanmyohtet.ecommerceservice.repository.ProductRepository;
import com.nyanmyohtet.ecommerceservice.repository.UserRepository;
import com.nyanmyohtet.ecommerceservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // Get the cart for a specific user
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id " + userId));
    }

    // Add a product to the cart
    public CartItem addProductToCart(Long userId, CartItem cartItem) {
        Cart cart = getOrCreateCart(userId); // Get or create the user's cart

        Product product = productRepository.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        cartItem.setCart(cart);
        cartItem.setProduct(product);

        return cartItemRepository.save(cartItem);
    }

    // Update the quantity of a product in the cart
    public CartItem updateProductInCart(Long userId, CartItem updatedCartItem) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id " + userId));

        CartItem existingCartItem = cartItemRepository.findByCartAndProductId(cart, updatedCartItem.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        existingCartItem.setQuantity(updatedCartItem.getQuantity());
        return cartItemRepository.save(existingCartItem);
    }

    // Remove a product from the cart
    public void removeProductFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id " + userId));

        CartItem cartItem = cartItemRepository.findByCartAndProductId(cart, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        cartItemRepository.delete(cartItem);
    }

    // Clear the cart for a user
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id " + userId));

        cartItemRepository.deleteAllByCart(cart);
    }

    // Get the cart for a specific user or create a new one if it doesn't exist
    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId)); // Create a new cart only if it doesn't exist
    }

    // Create a new cart for the user
    private Cart createNewCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }
}
