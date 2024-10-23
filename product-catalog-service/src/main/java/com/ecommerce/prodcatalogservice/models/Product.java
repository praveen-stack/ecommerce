package com.ecommerce.prodcatalogservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product extends BaseModel {

    private String title;
    private Double price;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne(targetEntity = Category.class)
    private Category category;
    private String image;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

}
