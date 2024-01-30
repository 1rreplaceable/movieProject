package com.example.order;

import com.example.order.cart.Cart;
import com.example.user.User;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrUpdateOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Long productId = orderRequest.getProductId();
            Integer quantity = orderRequest.getQuantity();
            orderService.createOrUpdateOrder(productId, quantity);
            return ResponseEntity.ok("Order created or updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating or updating order");
        }
    }

    @PostMapping("/delete")
    @Transactional
    public ResponseEntity<String> deleteSelectedItems(
            @RequestBody List<Long> productIds,
            HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        System.out.println("OrderController-User:"+user);
        System.out.println(productIds);
        try {
            orderService.deleteItems(productIds, user.getId().toString());
            return ResponseEntity.ok("Items deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting items");
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCartCount(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            int cartCount = orderService.getCartCount(user.getId().toString());

            return ResponseEntity.ok(cartCount);
        }

        // If user is not logged in, return 0
        return ResponseEntity.ok(0);
    }
    @PostMapping("/purchase")
    public ResponseEntity<String> purchase(@RequestBody List<Long> selectedProductIds, HttpSession httpSession) {
        System.out.println("OrderController=purchase 넘어옴");
        User user = (User) httpSession.getAttribute("user");

        try {
            orderService.updateOrder(selectedProductIds, user.getId());
            return ResponseEntity.ok("구매가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("구매에 실패했습니다.");
        }
    }
    @GetMapping("/completed-orders")
    public List<Cart> getCompletedOrders( HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        String userId = String.valueOf(user.getId());

        return orderService.getCompletedOrdersByUserId(userId);
    }
}
