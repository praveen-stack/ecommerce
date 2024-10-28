package com.ecommerce.cartservice.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class Product implements Serializable {
    private Long id;
    private String title;
    private Double price;
    private String description;
    private String image;
}
