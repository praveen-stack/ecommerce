package com.ecommerce.usermanagementservice.controllers;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.AuthorizedUser;
import com.ecommerce.usermanagementservice.dtos.ResetPassword;
import com.ecommerce.usermanagementservice.dtos.UserDto;
import com.ecommerce.usermanagementservice.dtos.UserProfileUpdateDto;
import com.ecommerce.usermanagementservice.mappers.UserDtoMapper;
import com.ecommerce.usermanagementservice.mappers.UserProfileUpdateDtoMapper;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.services.AuthService;
import com.ecommerce.usermanagementservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Autowired
    private UserProfileUpdateDtoMapper userProfileUpdateDtoMapper;

    @GetMapping
    public UserDto getProfile(Authentication authentication) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        User user = userService.getValidUserById(authUser.getId());
        return userDtoMapper.toDto(user);
    }

    @PutMapping("/reset-password")
    public UserDto resetPassword(Authentication authentication, @RequestBody @Valid ResetPassword dto) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        User user = authService.resetPassword(authUser, dto.getPassword());
        return userDtoMapper.toDto(user);
    }

    @PutMapping
    public UserDto updateProfile(Authentication authentication, @RequestBody @Valid UserProfileUpdateDto dto) throws UserExistsException {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        User user = userService.updateUser(authUser, userProfileUpdateDtoMapper.toEntity(dto));
        return userDtoMapper.toDto(user);
    }

}
