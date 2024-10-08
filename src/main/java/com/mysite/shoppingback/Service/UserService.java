package com.mysite.shoppingback.Service;

import com.mysite.shoppingback.DTO.UserDTO;
import com.mysite.shoppingback.domain.User;
import com.mysite.shoppingback.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    public UserDTO createUser(UserDTO userDTO) {
//        User user = new User();
//        user.setUserId(userDTO.getUserId());
//        user.setUsername(userDTO.getUsername());
//        user.setEmail(userDTO.getEmail());
//        user.setPassword("encoded_password"); // 비밀번호 암호화 필요
//        user.setAddress(userDTO.getAddress());
//        userRepository.save(user);
//        userDTO.setId(user.getId());
//        return userDTO;
//    }


    public UserDTO registerUser(UserDTO userDTO) {
        // 유저 ID 또는 이메일 중복 확인
        if (userRepository.findByUsername(userDTO.getUsername()) != null || userRepository.findByUserId(userDTO.getUserId()) != null) {
            throw new RuntimeException("User already exists.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // UserDTO를 User 엔티티로 변환
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setAddress(userDTO.getAddress());

        // 역할 설정 (기본적으로 ROLE_USER로 설정)
        Set<String> roles = userDTO.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = Collections.singleton("ROLE_USER"); // 기본 역할 설정
        }
        user.setRoles(roles);

        // 사용자 저장
        userRepository.save(user);

        // User 엔티티를 UserDTO로 변환하여 반환
        UserDTO registeredUser = new UserDTO();
        registeredUser.setId(user.getId());
        registeredUser.setUserId(user.getUserId());
        registeredUser.setUsername(user.getUsername());
        registeredUser.setEmail(user.getEmail());
        registeredUser.setAddress(user.getAddress());
        registeredUser.setRoles(user.getRoles());
        return registeredUser;
    }

    public Optional<UserDTO> loginUser(String userId, String password) {
        User user = userRepository.findByUserId(userId);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(new UserDTO(user.getId(), user.getUserId(), user.getUsername(), user.getEmail(), user.getAddress(), user.getPassword(), user.getRoles()));
        } else {
            return Optional.empty();
        }
    }

    // UserEntity를 UserDTO로 변환하는 메서드
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        // 기타 필요한 필드 설정
        return userDTO;
    }

    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    user.setAddress(updatedUser.getAddress());
                    return userRepository.save(user);
                });
    }

    User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return convertToDTO(user.get());
        } else {
            return null;
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}