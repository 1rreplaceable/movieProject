package com.example.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 예시: Spring Boot 컨트롤러
@RestController
@RequestMapping("/api")
public class ExampleController {

    @GetMapping("/example")
    public String getExample() {
        return "Hello from Spring Boot!";
    }

    // 다른 엔드포인트 및 메서드도 추가 가능
}