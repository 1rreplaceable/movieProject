package com.example.order;

import com.example.order.cart.Cart;
import com.example.product.Product;
import com.example.product.ProductService;
import com.example.user.User;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private ProductService productService;
    public void createOrUpdateOrder(Long productId, Integer quantity) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) httpSession.getAttribute("user");
        // 상품 정보 조회
        Optional<Product> productOptional = productService.getProductById(productId);
        Product product = productOptional.orElseThrow(() ->
                new RuntimeException("Product not found with id: " + productId));
        System.out.println(user);

        Order order = new Order();
        order.setUserId(user.getId().toString());
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setProductPrice(product.getPrice());
        order.setOrderDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        Order savedOrder = orderRepository.save(order);
    }

    public List<Cart> getCartItemsByUserId(String userId) {
        // 장바구니에 있는 상품 중 is_ordered가 false인 것만 가져오기
        List<Order> orders = orderRepository.findCartItemsByUserId(userId);

        // 장바구니 아이템 목록 생성
        List<Cart> cartItems = new ArrayList<>();

        for (Order order : orders) {
            Product product = productService.getProductById(order.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + order.getProductId()));

            // 해당 상품에 대한 장바구니 아이템 생성
            Cart cartItem = new Cart(
                    product.getProductId(),
                    product.getImagePath(),
                    product.getProductName(),
                    product.getProductDetail(),
                    order.getQuantity(),
                    product.getPrice()
            );

            cartItems.add(cartItem);
        }

        return cartItems;
    }
    @Transactional
    public void deleteItems(List<Long> productIds, String userId) {
        orderRepository.deleteByProductIdInAndUserId(productIds, userId);
    }

    public int getCartCount(String userId) {
        return orderRepository.countTotalProductsByUserId(userId);
    }

    @Transactional
    public void updateOrder(List<Long> selectedProductIds, Long userId) {
        // orders 테이블 업데이트 로직 구현
        orderRepository.updateOrder(selectedProductIds, userId);
    }

    public List<Cart> getCompletedOrdersByUserId(String userId) {
        // 주문 정보 조회 (is_ordered가 true인 것만)
        List<Order> completedOrders = orderRepository.findByUserIdAndIsOrderedTrue(userId);

        // 카트 아이템 리스트 생성
        List<Cart> completedOrderItems = new ArrayList<>();

        for (Order order : completedOrders) {
            Product product = productService.getProductById(order.getProductId()).orElseThrow();

            // 완료된 주문에 대한 카트 아이템 생성
            Cart cartItem = new Cart(
                    order.getProductId(),
                    product.getImagePath(),
                    product.getProductName(),
                    product.getProductDetail(),
                    order.getQuantity(),
                    product.getPrice()
            );

            completedOrderItems.add(cartItem);
        }

        return completedOrderItems;
    }

}