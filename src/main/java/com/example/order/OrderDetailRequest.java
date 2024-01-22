package com.example.order;

import lombok.Data;

@Data
public class OrderDetailRequest {

    private Long productId;
    private Integer quantity;
}
