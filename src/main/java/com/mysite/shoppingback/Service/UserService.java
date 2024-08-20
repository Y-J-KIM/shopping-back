package com.mysite.shoppingback.Service;

import com.mysite.shoppingback.DTO.UserDTO;
import com.mysite.shoppingback.domain.User;
import com.mysite.shoppingback.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword("encoded_password"); // 비밀번호 암호화 필요
        user.setAddress(userDTO.getAddress());
        userRepository.save(user);
        userDTO.setId(user.getId());
        return userDTO;
    }


    public UserDTO registerUser(UserDTO userDTO) {
        // 유저 ID 또는 이메일 중복 확인
        if (userRepository.findByUsername(userDTO.getUsername()) != null || userRepository.findByUserId(userDTO.getUserId()) != null) {
            throw new RuntimeException("User already exists.");
        }

        // 비밀번호 암호화
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));  // 비밀번호 암호화
        user.setAddress(userDTO.getAddress());

        userRepository.save(user);
        userDTO.setId(user.getId());
        return userDTO;
    }

    public Optional<UserDTO> loginUser(String userId, String password) {
        User user = userRepository.findByUserId(userId);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(new UserDTO(user.getId(), user.getUserId(), user.getUsername(), user.getEmail(), user.getAddress(), user.getPassword()));
        } else {
            return Optional.empty();
        }
    }
}
