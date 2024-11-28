package com.ecommerce.orderservice.dtos;

import com.ecommerce.orderservice.enums.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateOrderDto {
    @NotNull
    private Long shippingAddressId;
    @NotNull
    private Long billingAddressId;
    private PaymentMethod paymentMethod;
    @NotEmpty(message = "The items must not be empty")
    private List<CreateOrderItem> items;
}
