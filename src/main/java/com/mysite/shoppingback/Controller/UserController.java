package com.mysite.shoppingback.Controller;

import com.mysite.shoppingback.DTO.UserDTO;
import com.mysite.shoppingback.Service.UserService;
import com.mysite.shoppingback.domain.LoginRequest;
import com.mysite.shoppingback.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping
//    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
//        UserDTO createdUser = userService.createUser(userDTO);
//        return ResponseEntity.ok(createdUser);
//    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO registeredUser = userService.registerUser(userDTO);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<UserDTO> loginUser(@RequestParam String userId, @RequestParam String password) {
//        Optional<UserDTO> userDTO = userService.loginUser(userId, password);
//        if (userDTO.isPresent()) {
//            UserDTO user = userDTO.get();
//            //user.setPassword(null);  // 비밀번호를 응답에서 제거
//            return ResponseEntity.ok(user);
//        } else {
//            // 로그인 실패 시 오류 메시지 반환
//            return ResponseEntity.status(401).body(null);
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Optional<UserDTO> userDTO = userService.loginUser(loginRequest.getUserId(), loginRequest.getPassword());
        if (userDTO.isPresent()) {
            UserDTO user = userDTO.get();

            // 쿠키에 세션 토큰이나 유저 ID를 저장 (예: 단순 유저 ID 사용)
            Cookie cookie = new Cookie("userId", user.getUserId());
            cookie.setHttpOnly(true); // 보안을 위해 JavaScript에서 접근 불가하도록 설정
            cookie.setPath("/"); // 전체 경로에서 쿠키 사용 가능
            cookie.setMaxAge(60 * 60 * 24 * 7); // 쿠키 유효 기간 설정 (예: 7일)
            response.addCookie(cookie);

            // user.setPassword(null);  // 비밀번호를 응답에서 제거
            return ResponseEntity.ok(user);
        } else {
            // 로그인 실패 시 오류 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // 쿠키 삭제를 위해 유효 기간을 0으로 설정
        Cookie cookie = new Cookie("userId", null);
        cookie.setMaxAge(0);  // 유효 기간을 0으로 설정하여 즉시 삭제
        cookie.setPath("/");  // 쿠키의 경로를 설정 (전체 경로에서 삭제 가능)
        response.addCookie(cookie);

        // 로그아웃 완료 메시지를 반환
        return ResponseEntity.ok("Logout successful");
    }


}
