package com.mysite.shoppingback.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String userId;
    private String username;
    private String email;
    private String address;
    private String password;
    private Set<String> roles;

}
