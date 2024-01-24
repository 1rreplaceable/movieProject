package com.example.order;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "is_ordered")
    private boolean isOrdered;
}