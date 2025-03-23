package com.ecommerce.cartservice.controllers;

import com.ecommerce.cartservice.dtos.CartRequest;
import com.ecommerce.cartservice.dtos.CheckoutRequestDto;
import com.ecommerce.cartservice.dtos.CheckoutResponseDto;
import com.ecommerce.cartservice.dtos.AuthorizedUser;
import com.ecommerce.cartservice.dtos.CartDto;
import com.ecommerce.cartservice.dtos.ItemDto;
import com.ecommerce.cartservice.models.Cart;
import com.ecommerce.cartservice.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    private CartDto convertToDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setItems(cart.getItems().stream().map(item -> {
            ItemDto itemDto = new ItemDto();
            itemDto.setProductId(item.getProductId());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setItemTotal(item.getItemTotal());
            itemDto.setProduct(item.getProduct());
            return itemDto;
        }).toList());
        cartDto.setTotal(cart.getTotal());
        cartDto.setId(cart.getId());
        cartDto.setUserId(cart.getUserId());
        return cartDto;
    }

    @GetMapping
    public CartDto getCart(Authentication authentication) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        var cart = cartService.getCart(authUser);
        return convertToDto(cart);
    }

    @PutMapping("/items")
    public CartDto addToCart(Authentication authentication, @Valid @RequestBody CartRequest cartRequest) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        Cart cart;
        if(cartRequest.getQuantity() > 0){
            cart = cartService.addToCart(authUser, cartRequest.getProductId(), cartRequest.getQuantity());
        }
        else {
            // if negative or 0 remove from cart
            cart = cartService.removeFromCart(authUser, cartRequest.getProductId(), Optional.of(cartRequest.getQuantity() * -1));
        }
        return convertToDto(cart);
    }

    @PostMapping("/checkout")
    public CheckoutResponseDto checkout(Authentication authentication, @Valid @RequestBody CheckoutRequestDto checkoutRequest) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        return cartService.checkout(authUser, checkoutRequest);
    }
}
