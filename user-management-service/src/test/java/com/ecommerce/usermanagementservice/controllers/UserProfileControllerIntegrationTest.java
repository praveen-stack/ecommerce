package com.ecommerce.usermanagementservice.controllers;

import com.ecommerce.usermanagementservice.Exceptions.InvalidCredentialsException;
import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.ResetPassword;
import com.ecommerce.usermanagementservice.dtos.UserProfileUpdateDto;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.services.AuthService;
import com.ecommerce.usermanagementservice.utils.AuthUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserProfileControllerIntegrationTest {

    @Autowired
    private UserProfileController userController;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthUtil authUtil;

    @BeforeAll
    public void init() throws UserExistsException {
        User user = new User();
        user.setEmail("testusersignup@test.com");
        user.setPassword("pass");
        user.setName("test");
        authService.signup(user);

        User userToUpdateEmail = new User();
        userToUpdateEmail.setEmail("testusersignup1@test.com");
        userToUpdateEmail.setPassword("pass");
        userToUpdateEmail.setName("test");
        authService.signup(userToUpdateEmail);

        User passResetUser = new User();
        passResetUser.setEmail("testusersignup2@test.com");
        passResetUser.setPassword("pass");
        passResetUser.setName("test");
        authService.signup(passResetUser);
    }

    @Test
    public void GetProfileSuccess() throws InvalidCredentialsException {
        var loginDetails = authService.login("testusersignup@test.com", "pass");
        Authentication authentication = authUtil.getAuthentication(loginDetails.getUser());
        var user = userController.getProfile(authentication);
        assert user != null;
        assert user.getEmail().equals("testusersignup@test.com");
    }

    @Test
    public void updateProfileSuccess() throws UserExistsException, InvalidCredentialsException {
        var loginDetails = authService.login("testusersignup1@test.com", "pass");
        Authentication authentication = authUtil.getAuthentication(loginDetails.getUser());
        UserProfileUpdateDto dto = new UserProfileUpdateDto();
        dto.setEmail("testusersignup1updated@test.com");
        dto.setName("updatedname");

        var user = userController.updateProfile(authentication, dto);
        assert user != null;
        assert user.getEmail().equals("testusersignup1updated@test.com");
        assert user.getName().equals("updatedname");
    }


    @Test
    public void resetPasswordSuccess() throws InvalidCredentialsException {
        var loginDetails = authService.login("testusersignup2@test.com", "pass");
        Authentication authentication = authUtil.getAuthentication(loginDetails.getUser());
        ResetPassword dto = new ResetPassword();
        dto.setPassword("newpass");
        var user = userController.resetPassword(authentication, dto);
        assert user != null;
        var newLoginDetails = authService.login("testusersignup2@test.com", "newpass");
        assert newLoginDetails != null;
    }




}
