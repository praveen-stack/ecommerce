package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.models.User;

public interface AuthService {

    User signup(User user) throws UserExistsException;

}
