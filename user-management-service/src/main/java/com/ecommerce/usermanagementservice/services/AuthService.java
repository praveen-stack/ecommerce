package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.InvalidCredentialsException;
import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.AuthenticatedUser;
import com.ecommerce.usermanagementservice.models.User;

public interface AuthService {

    User signup(User user) throws UserExistsException;
    AuthenticatedUser login(String email, String password) throws InvalidCredentialsException;

}
