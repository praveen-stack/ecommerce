package com.ecommerce.cartservice.dtos;

import com.ecommerce.cartservice.models.Cart;
import org.springframework.security.core.Transient;

import java.io.Serializable;

@Transient
public class CartCache extends Cart implements Serializable {
    public CartCache(Long userId) {
        super(userId);
    }
}
