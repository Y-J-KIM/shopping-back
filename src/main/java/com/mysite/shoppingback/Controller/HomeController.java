package com.mysite.shoppingback.Controller;

import com.mysite.shoppingback.DTO.UserDTO;
import com.mysite.shoppingback.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<?> home(@CookieValue(value = "userId", defaultValue = "") String userId) {
        if (userId.isEmpty()) {
            // 쿠키에 유저 정보가 없으면 401 Unauthorized 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        // 유저 ID로 사용자 정보 조회
        Optional<UserDTO> userDTO = userService.findUserById(userId);
        if (userDTO.isPresent()) {
            // 로그인된 상태라면 유저 정보 반환
            return ResponseEntity.ok(userDTO.get());
        } else {
            // 유효하지 않은 유저 ID인 경우 401 Unauthorized 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user");
        }
    }
}

