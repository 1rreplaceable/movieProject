package com.example.order;

import com.example.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private HttpSession httpSession;

    public void createOrUpdateOrder(Long productId, Integer quantity, Integer totalPrice) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) httpSession.getAttribute("user");
        System.out.println(user);
        // 주문 생성 또는 갱신 로직
        Order order = new Order();
        order.setUserId(user.getId());
        order.setTotalPrice(totalPrice);
        order.setOrderDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));


        Order savedOrder = orderRepository.save(order);

        // 주문 상세 생성
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(savedOrder);
        orderDetail.setProductId(productId);
        orderDetail.setQuantity(quantity);

        orderDetailRepository.save(orderDetail);
    }
}