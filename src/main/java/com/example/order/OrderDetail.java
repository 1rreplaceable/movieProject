package com.example.order;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private int quantity;

}
