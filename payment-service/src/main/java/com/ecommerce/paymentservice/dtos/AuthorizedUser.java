package com.ecommerce.paymentservice.dtos;

import lombok.Data;

@Data
public class AuthorizedUser {
    private Long id;
    private String email;
    private String phoneNumber;
    private String name;
}
