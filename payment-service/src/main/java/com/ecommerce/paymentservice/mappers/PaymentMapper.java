package com.ecommerce.paymentservice.mappers;

import com.ecommerce.paymentservice.dtos.PaymentDto;
import com.ecommerce.paymentservice.models.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper extends com.ecommerce.prodcatalogservice.components.ObjectMapper<PaymentDto, Payment> {
    @Override
    public Class getDtoClass() {
        return PaymentDto.class;
    }

    @Override
    public Class getEntityClass() {
        return Payment.class;
    }
}
