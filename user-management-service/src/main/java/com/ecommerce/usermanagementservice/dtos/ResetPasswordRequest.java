package com.ecommerce.usermanagementservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank()
    @Email()
    private String email;
}
