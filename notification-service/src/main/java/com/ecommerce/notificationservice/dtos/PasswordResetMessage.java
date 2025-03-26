package com.ecommerce.notificationservice.dtos;

import java.io.Serializable;
import lombok.Data;

@Data
public class PasswordResetMessage implements Serializable{
    private String email;
    private String token;
}
