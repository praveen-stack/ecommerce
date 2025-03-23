package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.AuthorizedUser;
import com.ecommerce.usermanagementservice.models.Address;
import com.ecommerce.usermanagementservice.models.User;
import java.util.List;

public interface UserService {
    User getUserByEmail(String email);

    User updateUser(AuthorizedUser user, User userInput) throws UserExistsException;

    Address updateAddress(AuthorizedUser user, Address address);

    List<Address> getAddresses(AuthorizedUser user);

    void deleteAddress(AuthorizedUser user, Long addressId);

    User getValidUserById(Long id);

    Address getAddressById(AuthorizedUser user, Long addressId);
}
