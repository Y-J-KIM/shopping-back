package com.mysite.shoppingback.Controller;

import com.mysite.shoppingback.DTO.UserDTO;
import com.mysite.shoppingback.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO registeredUser = userService.registerUser(userDTO);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestParam String userId, @RequestParam String password) {
        Optional<UserDTO> userDTO = userService.loginUser(userId, password);
        if (userDTO.isPresent()) {
            UserDTO user = userDTO.get();
            user.setPassword(null);  // 비밀번호를 응답에서 제거
            return ResponseEntity.ok(user);
        } else {
            // 로그인 실패 시 오류 메시지 반환
            return ResponseEntity.status(401).body(null);
        }
    }
}
