package com.example.user;

// UserController.java

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        System.out.println("Signup 요청 받음");
        // 사용자가 이미 존재하는지 확인
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 사용자");
        }

        // 회원가입 처리
        userService.registerUser(user);
        return ResponseEntity.ok("회원가입 성공");
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpSession session) {
        Optional<User> optionalUser = userService.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            User storedUser = optionalUser.get();

            if (storedUser.getPassword().equals(user.getPassword())) {
                // 로그인 성공 시 세션에 사용자 정보 저장
                session.setAttribute("user", storedUser);
                return ResponseEntity.ok("로그인 성공");
            } else {
                return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
            }
        } else {
            return ResponseEntity.badRequest().body("존재하지 않는 사용자");
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        return ResponseEntity.ok("로그아웃 성공");
    }

}
