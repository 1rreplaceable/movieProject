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

                return ResponseEntity.ok(storedUser.getId()+"");
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

        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/check-login")
    public ResponseEntity<Boolean> checkLogin(HttpSession httpSession) {
        // 세션에서 사용자 정보를 확인하고 로그인 상태를 반환
        User user = (User) httpSession.getAttribute("user");
        boolean isLoggedIn = user != null;
        return ResponseEntity.ok(isLoggedIn);
    }

    @GetMapping("/info")
    public ResponseEntity<User> getUserInfo(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        Optional<User> currentUser = userService.getCurrentUser(user.getId());
        return currentUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserInfo(HttpSession httpSession, @RequestBody User updatedUser) {
        User user = (User) httpSession.getAttribute("user");
        Optional<User> currentUser = userService.getCurrentUser(user.getId());
        if (currentUser.isPresent()) {
            User existingUser = currentUser.get();
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword());
            userService.updateUser(existingUser);
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/id")
    public String getUserIdFromSession(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        return user.getUsername();
    }

}

