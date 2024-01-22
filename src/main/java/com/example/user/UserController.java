package com.example.user;

// UserController.java

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 실제 프로덕션에서는 더 강력한 토큰 생성 로직을 사용해야 합니다.
    private String generateToken() {
        // 간단하게 UUID 등을 사용하는 것이 아니라 보안적으로 안전한 토큰을 생성해야 합니다.
        return "exampleToken";
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
    public ResponseEntity<String> login(@RequestBody User user, HttpSession session, HttpServletResponse response) {
        Optional<User> optionalUser = userService.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            User storedUser = optionalUser.get();

            if (storedUser.getPassword().equals(user.getPassword())) {
                // 로그인 성공 시 세션에 사용자 정보 저장
                session.setAttribute("user", storedUser);

                // 쿠키 생성 및 클라이언트에 전송
                Cookie userCookie = new Cookie("userId", storedUser.getId().toString());
                userCookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 설정 (예: 1일)
                response.addCookie(userCookie);

                return ResponseEntity.ok("로그인 성공"+storedUser);
            } else {
                return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
            }
        } else {
            return ResponseEntity.badRequest().body("존재하지 않는 사용자");
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session, HttpServletResponse response) {
        // 세션 무효화
        session.invalidate();

        Cookie cookie = new Cookie("userId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("로그아웃 성공");
    }

}

