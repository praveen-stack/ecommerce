package com.ecommerce.usermanagementservice.controllers;

import com.ecommerce.usermanagementservice.Exceptions.InvalidCredentialsException;
import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.UserLoginDto;
import com.ecommerce.usermanagementservice.dtos.UserSignupDto;
import com.ecommerce.usermanagementservice.enums.MessageText;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
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
        dto.setEmail("te33stuser@test.com");
        dto.setPassword("abdde@!#!@#d3#");
        dto.setName("testuser");
        var user = authController.signup(dto);
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

    @Test
    public void Login_User() throws InvalidCredentialsException {
        UserSignupDto dto = new UserSignupDto();
        dto.setEmail("testuser@test.com");
        dto.setPassword("abdde@!#!@#d3#");
        dto.setName("testuser");
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail(dto.getEmail());
        loginDto.setPassword(dto.getPassword());
        var responseEntity = authController.login(loginDto);
        assertEquals(responseEntity.getBody().getEmail(), loginDto.getEmail());
        assertNotNull(responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE));
    }
    @Test
    public void Login_Failed_User_Not_Registered() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("abc@test.com");
        loginDto.setPassword("abdde@!#!@#d3#");
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            authController.login(loginDto);
        });
        assertEquals(exception.getMessage(), MessageText.INCORRECT_EMAIL_PASSWORD.getValue());
    }

    @Test
    public void Login_Failed_Password_Mismatch() {
        UserSignupDto dto = new UserSignupDto();
        dto.setEmail("test2user@test.com");
        dto.setPassword("abdde@!#!@#d3#");
        dto.setName("testuser");
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail(dto.getEmail());
        loginDto.setPassword("wrong password");
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            authController.login(loginDto);
        });
        assertEquals(exception.getMessage(), MessageText.INCORRECT_EMAIL_PASSWORD.getValue());
    }

}
