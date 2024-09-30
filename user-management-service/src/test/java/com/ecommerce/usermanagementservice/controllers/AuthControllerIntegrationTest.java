package com.ecommerce.usermanagementservice.controllers;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.UserSignupDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private AuthController authController;

    @Test
    public void Signup_Success_Test() throws UserExistsException {
        UserSignupDto dto = new UserSignupDto();
        dto.setEmail("testuser@test.com");
        dto.setPassword("abdde@!#!@#d3#");
        dto.setName("testuser");
        var user = authController.signup(dto);
        assertEquals(user.getId(), 1L);
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getName(), dto.getName());
    }

    @Test
    public void Signup_User_Exists() throws UserExistsException {
        UserSignupDto dto = new UserSignupDto();
        dto.setEmail("testuser@test.com");
        dto.setPassword("abdde@!#!@#d3#");
        dto.setName("testuser");
        authController.signup(dto);
        UserExistsException exception = assertThrows(UserExistsException.class, () -> {
            authController.signup(dto);
        });
        assertEquals(exception.getMessage(), "User with email already exists");
    }
}
