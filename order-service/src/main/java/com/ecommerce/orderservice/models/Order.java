package com.ecommerce.orderservice.models;

import com.ecommerce.orderservice.enums.OrderStatus;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.List;

@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "`order`")
public class Order extends BaseModel {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = true)
    private Double totalAmount;

    @Column(nullable = true)
    private Long paymentId;

    @Type(JsonType.class)
    @Column(columnDefinition = "json", nullable = false)
    private Address shippingAddress;

    @Type(JsonType.class)
    @Column(columnDefinition = "json", nullable = false)
    private Address billingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> items;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;


}
