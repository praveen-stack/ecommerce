package com.ecommerce.usermanagementservice.dtos;

import lombok.Data;

@Data
public class PasswordResetMessage {
    private String email;
    private String token;
}
