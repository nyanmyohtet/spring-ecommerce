package com.nyanmyohtet.ecommerceservice.service;

import com.nyanmyohtet.ecommerceservice.enums.OrderStatus;
import com.nyanmyohtet.ecommerceservice.model.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);
    Order getOrderById(Long orderId);
    List<Order> getOrdersByUserId(Long userId);
    Order updateOrderStatus(Long orderId, OrderStatus status);
    void deleteOrder(Long orderId);
}
