package com.mysite.shoppingback.Controller;

import com.mysite.shoppingback.DTO.UserDTO;
import com.mysite.shoppingback.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/home")
    public ResponseEntity<String> home(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false: 세션이 없으면 null 반환

        if (session != null && session.getAttribute("userId") != null) {
            String userId = (String) session.getAttribute("userId");
            return ResponseEntity.ok("Welcome, " + userId + "!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in.");
        }
    }

}

