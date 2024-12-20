package com.ecommerce.usermanagementservice.controllers;

import com.ecommerce.usermanagementservice.Exceptions.InvalidCredentialsException;
import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.*;
import com.ecommerce.usermanagementservice.mappers.UserDtoMapper;
import com.ecommerce.usermanagementservice.mappers.UserSignupDtoMapper;
import com.ecommerce.usermanagementservice.services.AuthService;
import com.ecommerce.usermanagementservice.utils.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserSignupDtoMapper userSignupDtoMapper;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/signup")
    public UserDto signup(@Valid @RequestBody UserSignupDto dto) throws UserExistsException {
        var userInput = userSignupDtoMapper.toEntity(dto);
        var user = this.authService.signup(userInput);
        var userDto = this.userDtoMapper.toDto(user);
        return userDto;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody UserLoginDto dto) throws InvalidCredentialsException {
        var authenticatedUser = this.authService.login(dto.getEmail(), dto.getPassword());
        var userDto = this.userDtoMapper.toDto(authenticatedUser.getUser());
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, authUtil.createLoginCookie(authenticatedUser.getToken()).toString());
        var responseEntity = new ResponseEntity<UserDto>(userDto, headers, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/reset-password-token")
    public ResponseEntity<Void> requestResetPasswordToken(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        this.authService.generatePasswordResetToken(resetPasswordRequest.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordUsingToken passwordResetRequest) {
        this.authService.resetPassword(passwordResetRequest.getToken(), passwordResetRequest.getPassword());
        return ResponseEntity.noContent().build();
    }
}
