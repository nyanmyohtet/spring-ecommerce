package com.nyanmyohtet.ecommerceservice.service.Impl;

import com.nyanmyohtet.ecommerceservice.enums.OrderStatus;
import com.nyanmyohtet.ecommerceservice.exception.BadRequestException;
import com.nyanmyohtet.ecommerceservice.exception.ResourceNotFoundException;
import com.nyanmyohtet.ecommerceservice.model.Cart;
import com.nyanmyohtet.ecommerceservice.model.CartItem;
import com.nyanmyohtet.ecommerceservice.model.Order;
import com.nyanmyohtet.ecommerceservice.model.OrderItem;
import com.nyanmyohtet.ecommerceservice.repository.CartItemRepository;
import com.nyanmyohtet.ecommerceservice.repository.CartRepository;
import com.nyanmyohtet.ecommerceservice.repository.OrderRepository;
import com.nyanmyohtet.ecommerceservice.repository.UserRepository;
import com.nyanmyohtet.ecommerceservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    // Place a new order for a user
    @Transactional
    public Order placeOrder(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id " + userId));

        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty. Cannot place an order.");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PLACED);

        // Create order items and set them to the order
        List<OrderItem> orderItems = createOrderItemsFromCart(cart, order);
        order.setOrderItems(orderItems);

        // Calculate the total price
        BigDecimal totalPrice = calculateTotalPrice(orderItems);
        order.setTotalPrice(totalPrice);

        // Save the order
        order = orderRepository.save(order);

        cartItemRepository.deleteAllByCart(cart); // Clear the cart after order is placed

        return order;
    }

    // Retrieve order by order ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
    }

    // Retrieve all orders for a specific user
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Update the status of an order
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Delete an order
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
        orderRepository.delete(order);
    }

    // Create order items from cart items
    private List<OrderItem> createOrderItemsFromCart(Cart cart, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setOrder(order); // Set the order reference

            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
