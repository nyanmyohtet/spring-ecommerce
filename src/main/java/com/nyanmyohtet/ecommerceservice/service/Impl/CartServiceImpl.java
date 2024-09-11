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
    public Cart addProductToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Check if sufficient stock is available
        if (product.getQuantityInStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock available for product: " + product.getName());
        }

        // Check if the user has an existing cart
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        // Check if the product is already in the cart
        CartItem existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (existingCartItem != null) {
            // If the product is already in the cart, update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartItemRepository.save(existingCartItem);
        } else {
            // If the product is not in the cart, create a new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        // Decrease the product's quantityInStock
        product.setQuantityInStock(product.getQuantityInStock() - quantity);
        productRepository.save(product);

        return cart;
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
    public Cart removeProductFromCart(Long userId, Long productId) {
        // Get user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Get product by ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Check if the user has an existing cart
        Cart cart = user.getCart();
        if (cart == null) {
            throw new IllegalArgumentException("User has no active cart");
        }

        // Find the CartItem associated with the product
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Product not found in the cart");
        }

        // Increase product quantityInStock by the quantity of the item being removed from the cart
        int removedQuantity = cartItem.getQuantity();
        product.setQuantityInStock(product.getQuantityInStock() + removedQuantity);
        productRepository.save(product);

        // Remove the CartItem from the cart
        cartItemRepository.delete(cartItem);

        return cart;
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
