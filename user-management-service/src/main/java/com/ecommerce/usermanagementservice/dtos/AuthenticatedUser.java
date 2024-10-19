package com.ecommerce.usermanagementservice.dtos;

import com.ecommerce.usermanagementservice.models.User;
import lombok.Data;

@Data
public class AuthenticatedUser {
    private User user;
    private String token;
}
