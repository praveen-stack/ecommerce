package com.ecommerce.cartservice.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {
    private String recipientName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phoneNumber;
}
