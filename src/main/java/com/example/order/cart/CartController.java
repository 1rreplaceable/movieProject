package com.example.order.cart;

import com.example.order.OrderService;
import com.example.user.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private OrderService orderService;
    @GetMapping
    public ResponseEntity<List<Cart>> getCartItems(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<Cart> cartItems = orderService.getCartItemsByUserId(user.getId().toString());
        return ResponseEntity.ok(cartItems);
    }


}
