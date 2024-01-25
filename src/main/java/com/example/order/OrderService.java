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
        // 주문 정보 조회
        List<Order> orders = orderRepository.findByUserId(userId);

        // 주문 정보를 바탕으로 Cart 리스트 생성
        Map<Long, Cart> cartMap = new HashMap<>();

        for (Order order : orders) {
            Product product = productService.getProductById(order.getProductId()).orElseThrow();

            // 이미 해당 상품에 대한 Cart가 존재하는 경우 수량을 더함
            if (cartMap.containsKey(product.getProductId())) {
                Cart existingCart = cartMap.get(product.getProductId());
                existingCart.setQuantity(existingCart.getQuantity() + order.getQuantity());
            } else {
                // 해당 상품에 대한 Cart가 존재하지 않는 경우 새로운 Cart 생성
                Cart newCart = new Cart(
                        product.getProductId(),
                        product.getImagePath(),
                        product.getProductName(),
                        product.getProductDetail(),
                        order.getQuantity(),
                        product.getPrice()
                );
                cartMap.put(product.getProductId(), newCart);
            }
        }

        // Map의 값들을 리스트로 변환
        List<Cart> cartItems = new ArrayList<>(cartMap.values());

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

}