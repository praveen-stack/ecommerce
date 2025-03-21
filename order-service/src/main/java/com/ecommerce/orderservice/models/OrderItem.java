package com.ecommerce.orderservice.models;

import jakarta.persistence.*;
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
public class OrderItem extends BaseModel {

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String productTitle;

    @Column(nullable = false)
    private String productImage;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String productDescription;

    @ManyToOne()
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

}
