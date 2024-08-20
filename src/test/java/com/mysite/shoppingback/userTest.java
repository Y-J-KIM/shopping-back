package com.mysite.shoppingback;

import com.mysite.shoppingback.DTO.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class userTest {

    @Autowired
    private TestRestTemplate restTemplate;

//    @Test
//    public void createUserTest() {
//        UserDTO userDTO = new UserDTO(null, "testuser", "test@example.com", "1234 Test Address");
//        ResponseEntity<UserDTO> response = restTemplate.postForEntity("/api/users", userDTO, UserDTO.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody().getId()).isNotNull();
//        assertThat(response.getBody().getAddress()).isEqualTo("1234 Test Address");
//    }
}
