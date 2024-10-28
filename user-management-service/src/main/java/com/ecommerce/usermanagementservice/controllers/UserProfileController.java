package com.ecommerce.usermanagementservice.controllers;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.*;
import com.ecommerce.usermanagementservice.mappers.AddressDtoMapper;
import com.ecommerce.usermanagementservice.mappers.UserDtoMapper;
import com.ecommerce.usermanagementservice.mappers.UserProfileUpdateDtoMapper;
import com.ecommerce.usermanagementservice.models.Address;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.services.AuthService;
import com.ecommerce.usermanagementservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @Autowired
    private AddressDtoMapper addressDtoMapper;

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

    @DeleteMapping("/addresses/{id}")
    public void deleteAddress(Authentication authentication, @PathVariable Long id) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        userService.deleteAddress(authUser, id);
    }
    @GetMapping("/addresses")
    public List<AddressDto> getAddresses(Authentication authentication) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        List<Address> addresses = userService.getAddresses(authUser);
        return addresses.stream().map(addressDtoMapper::toDto).toList();
    }
    @PutMapping("/addresses")
    public AddressDto updateAddress(Authentication authentication, @RequestBody @Valid AddressDto dto) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        Address address = userService.updateAddress(authUser, addressDtoMapper.toEntity(dto));
        return addressDtoMapper.toDto(address);
    }
}
