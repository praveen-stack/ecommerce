package com.ecommerce.usermanagementservice.Exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String message){
        super(message);
    }
}
