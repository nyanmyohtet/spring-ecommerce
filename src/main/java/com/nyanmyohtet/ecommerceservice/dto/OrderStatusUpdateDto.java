package com.nyanmyohtet.ecommerceservice.dto;

import com.nyanmyohtet.ecommerceservice.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateDto {
    private OrderStatus status;
}
