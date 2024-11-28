package com.ecommerce.usermanagementservice.dtos;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String email;
    private String phoneNumber;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private List<AddressDto> addresses;
}
