package com.ecommerce.usermanagementservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Address extends BaseModel{
    @Column(nullable = false)
    private String recipientName;
    @Column(nullable = false)
    private String addressLine1;
    private String addressLine2;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String postalCode;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String phoneNumber;
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
