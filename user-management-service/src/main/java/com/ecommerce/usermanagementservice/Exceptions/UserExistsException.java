package com.ecommerce.usermanagementservice.Exceptions;

public class UserExistsException extends Exception {
    public UserExistsException(String message){
        super(message);
    }
}
