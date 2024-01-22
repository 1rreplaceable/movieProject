package com.example.order;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpSession httpSession;

    @PostMapping
    public void createOrUpdateOrder(@RequestBody OrderRequest orderRequest) {
        Long productId = orderRequest.getProductId();
        Integer quantity = orderRequest.getQuantity();
        Integer totalPrice = orderRequest.getTotalPrice();

        orderService.createOrUpdateOrder(productId, quantity, totalPrice);
    }
}
