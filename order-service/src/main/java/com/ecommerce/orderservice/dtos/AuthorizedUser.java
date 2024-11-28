package com.ecommerce.orderservice.dtos;

import lombok.Data;

@Data
public class AuthorizedUser {
    private Long id;
    private String email;
    private String name;
}
