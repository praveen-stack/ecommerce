package com.ecommerce.cartservice.dtos;

import com.ecommerce.cartservice.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequestDto {

    @NotNull
    private Long shippingAddressId;
    @NotNull
    private Long billingAddressId;
    @NotNull(message = "Payment method must be one of: CREDIT_CARD, DEBIT_CARD, PAYPAL, UPI")
    private PaymentMethod paymentMethod;
}
