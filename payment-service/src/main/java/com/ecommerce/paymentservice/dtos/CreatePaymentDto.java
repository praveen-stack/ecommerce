package com.ecommerce.paymentservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentDto {
    @NotNull
    private Long orderId;
    @NotNull
    private Double amount;
}
