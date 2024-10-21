package com.ecommerce.usermanagementservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPassword {
    @NotBlank()
    @Size(min = 8, max = 16)
    private String password;
}
