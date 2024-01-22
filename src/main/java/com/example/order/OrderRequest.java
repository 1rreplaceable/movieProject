
package com.example.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private Long productId;
    private Integer quantity;
    private Integer totalPrice;
    private List<OrderDetailRequest> orderDetails;
}

