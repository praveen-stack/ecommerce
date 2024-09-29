package com.ecommerce.usermanagementservice.dtos;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
