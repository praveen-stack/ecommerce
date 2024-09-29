package com.ecommerce.usermanagementservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UserSignupDto {

    @NotBlank()
    @Email()
    private String email;

    @NotBlank()
    @Size(min = 8, max = 16)
    private String password;

    @NotBlank()
    @Size(min = 1)
    private String name;
}
