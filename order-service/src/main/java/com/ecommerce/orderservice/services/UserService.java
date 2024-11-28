package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.dtos.AuthorizedUser;
import com.ecommerce.orderservice.models.Address;

public interface UserService {
    Address getAddressById(Long addressId, AuthorizedUser user);
}
