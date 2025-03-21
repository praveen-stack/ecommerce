package com.ecommerce.paymentservice.dtos;

import com.ecommerce.paymentservice.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentDto {
    @NotNull
    private Long orderId;
    @NotNull
    private Double amount;
    private PaymentMethod paymentMethod;
}
