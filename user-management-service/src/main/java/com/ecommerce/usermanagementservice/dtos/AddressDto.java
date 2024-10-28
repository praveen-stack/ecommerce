package com.ecommerce.usermanagementservice.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    @NotBlank
    private String recipientName;
    @NotBlank
    private String addressLine1;
    private String addressLine2;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String country;
    @NotBlank
    private String phoneNumber;
}
