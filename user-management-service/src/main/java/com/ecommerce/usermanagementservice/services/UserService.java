package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.AuthorizedUser;
import com.ecommerce.usermanagementservice.models.User;

public interface UserService {
    User getUserByEmail(String email);

    User updateUser(AuthorizedUser user, User userInput) throws UserExistsException;

    User getValidUserById(Long id);
}
