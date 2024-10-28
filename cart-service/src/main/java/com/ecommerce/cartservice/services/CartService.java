package com.ecommerce.cartservice.services;

import com.ecommerce.cartservice.dtos.AuthorizedUser;
import com.ecommerce.cartservice.models.Cart;
import java.util.Optional;

public interface CartService {
    Cart addToCart(AuthorizedUser user, Long productId, Integer quantity);
    Cart removeFromCart(AuthorizedUser user, Long productId, Optional<Integer> quantityOptional);
    Cart getCart(AuthorizedUser user);
}
