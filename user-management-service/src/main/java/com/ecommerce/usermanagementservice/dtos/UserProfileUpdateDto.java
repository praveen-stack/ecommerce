package com.ecommerce.usermanagementservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileUpdateDto {
    @NotBlank()
    @Email()
    private String email;

    @NotBlank()
    @Size(min = 1)
    private String name;

    @NotBlank()
    @Size(min = 10)
    private String phoneNumber;
}
