package com.ecommerce.paymentservice.models;

import com.ecommerce.paymentservice.enums.Gateway;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Payment extends BaseModel {
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long orderId;
    @Column(nullable = false)
    private Gateway gateway;
    @Column(nullable = false)
    private String gatewayPaymentId;

    @Column(length = 2048)
    private String paymentLink;

    @Column(nullable = false)
    private PaymentStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}
