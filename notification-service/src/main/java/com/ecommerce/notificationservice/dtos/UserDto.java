package com.ecommerce.notificationservice.dtos;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String email;
    private String phoneNumber;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;
}
