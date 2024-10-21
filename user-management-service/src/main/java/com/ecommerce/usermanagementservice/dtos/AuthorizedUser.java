package com.ecommerce.usermanagementservice.dtos;

import lombok.Data;

@Data
public class AuthorizedUser {
    private Long id;
    private String email;
    private String name;
}
