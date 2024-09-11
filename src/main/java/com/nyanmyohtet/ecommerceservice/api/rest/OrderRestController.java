package com.nyanmyohtet.ecommerceservice.api.rest;

import com.nyanmyohtet.ecommerceservice.dto.OrderStatusUpdateDto;
import com.nyanmyohtet.ecommerceservice.model.Order;
import com.nyanmyohtet.ecommerceservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/order-management/orders")
public class OrderRestController {

    private final OrderService orderService;

    // Place a new order for a user
    @PostMapping("/place/{userId}")
    public ResponseEntity<Order> placeOrder(@PathVariable Long userId) {
        Order placedOrder = orderService.placeOrder(userId);
        return ResponseEntity.ok(placedOrder);
    }

    // Get order details by order ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // Get all orders for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    // Update the status of an order (e.g., cancel, complete)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateDto orderStatusUpdate) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, orderStatusUpdate.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }

    // Delete an order by ID
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
