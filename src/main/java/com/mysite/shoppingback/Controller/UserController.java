package com.mysite.shoppingback.Controller;

import com.mysite.shoppingback.DTO.UserDTO;
import com.mysite.shoppingback.Service.UserService;
import com.mysite.shoppingback.domain.LoginRequest;
import com.mysite.shoppingback.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<UserDTO> loginUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        Optional<UserDTO> userDTO = userService.loginUser(loginRequest.getUserId(), loginRequest.getPassword());
        if (userDTO.isPresent()) {
            UserDTO user = userDTO.get();

            // 세션에 사용자 정보를 저장
            HttpSession session = request.getSession();
            session.setAttribute("user", user);  // 'user'는 세션에서 사용자 정보를 가져올 때 사용할 키입니다.
            System.out.println("저장된 아이디: "+ user.getUserId());
            session.setAttribute("userId", user.getUserId());

            // 쿠키에 세션 ID를 저장 (JSESSIONID는 기본적으로 서블릿 컨테이너가 자동으로 관리)
            // 이 부분은 생략할 수 있습니다. JSESSIONID는 컨테이너에서 자동으로 관리됩니다.

            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<UserDTO> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
//        Optional<UserDTO> userDTO = userService.loginUser(loginRequest.getUserId(), loginRequest.getPassword());
//        if (userDTO.isPresent()) {
//            UserDTO user = userDTO.get();
//
//            // 쿠키에 세션 토큰이나 유저 ID를 저장 (예: 단순 유저 ID 사용)
//            Cookie cookie = new Cookie("JSESSIONID", user.getUserId());
//            cookie.setHttpOnly(true); // 보안을 위해 JavaScript에서 접근 불가하도록 설정
//            cookie.setPath("/"); // 전체 경로에서 쿠키 사용 가능
//            cookie.setMaxAge(60 * 60 * 24 * 7); // 쿠키 유효 기간 설정 (예: 7일)
//            response.addCookie(cookie);
//
//            // user.setPassword(null);  // 비밀번호를 응답에서 제거
//            return ResponseEntity.ok(user);
//        } else {
//            // 로그인 실패 시 오류 메시지 반환
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//    }

//    @GetMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletResponse response) {
//        // 쿠키 삭제를 위해 유효 기간을 0으로 설정
//        Cookie cookie = new Cookie("userId", null);
//        cookie.setMaxAge(0);  // 유효 기간을 0으로 설정하여 즉시 삭제
//        cookie.setPath("/");  // 쿠키의 경로를 설정 (전체 경로에서 삭제 가능)
//        response.addCookie(cookie);
//
//        // 로그아웃 완료 메시지를 반환
//        return ResponseEntity.ok("Logout successful");
//    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false); // 세션이 존재하면 가져옴, 없으면 null
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        // 세션 쿠키를 클라이언트 측에서 제거
        Cookie cookie = new Cookie("SESSIONID", null);
        cookie.setPath("/"); // 전체 경로에서 쿠키를 사용할 수 있게 설정
        cookie.setMaxAge(0); // 쿠키 만료 시간 설정 (즉시 만료)
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        // 사용자를 ID로 조회하여 반환하는 로직
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
        // 세션에서 사용자 정보를 가져옴
        HttpSession session = request.getSession(false); // 세션이 없으면 null을 반환
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserDTO currentUser = (UserDTO) session.getAttribute("user");

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(currentUser);
    }


}
