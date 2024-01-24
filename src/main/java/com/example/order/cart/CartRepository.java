package com.example.order.cart;

import com.example.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Order, Cart> {

}

