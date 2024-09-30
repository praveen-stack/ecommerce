package com.ecommerce.usermanagementservice.controllers;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.UserDto;
import com.ecommerce.usermanagementservice.dtos.UserSignupDto;
import com.ecommerce.usermanagementservice.mappers.UserDtoMapper;
import com.ecommerce.usermanagementservice.mappers.UserSignupDtoMapper;
import com.ecommerce.usermanagementservice.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/signup")
    public UserDto signup(@Valid @RequestBody UserSignupDto dto) throws UserExistsException {
        var userInput = userSignupDtoMapper.toEntity(dto);
        var user = this.authService.signup(userInput);
        var userDto = this.userDtoMapper.toDto(user);
        return userDto;
    }
}
