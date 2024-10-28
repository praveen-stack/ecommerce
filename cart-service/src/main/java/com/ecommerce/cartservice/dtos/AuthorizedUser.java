package com.ecommerce.cartservice.dtos;

import lombok.Data;

@Data
public class AuthorizedUser {
    private Long id;
    private String email;
    private String name;
    private String jwt;
}
