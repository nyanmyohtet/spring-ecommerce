package com.nyanmyohtet.ecommerceservice.dto;

import lombok.Data;

@Data
public class AddProductToCartDto {
    private Long productId;
    private int quantity;
}
