package com.ecommerce.cartservice.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Document(collection = "carts")
public class Cart implements java.io.Serializable {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long userId;

    private List<Item> items;

    @Transient
    private Double total;

    public Cart(Long userId){
        this.userId = userId;
        this.items = new ArrayList<Item>();
        this.total = 0d;
    }

    public void addToCart(Long productId, Integer quantity){
        for (Item item : this.items) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        Item newItem = new Item();
        newItem.setProductId(productId);
        newItem.setQuantity(quantity);
        this.items.add(newItem);
    }

    public void remove(Long productId, Optional<Integer> quantityOptional) {
        if (quantityOptional.isPresent()) {
            Integer quantity = quantityOptional.get();
            this.items.forEach(item -> {
                if (item.getProductId().equals(productId)) {
                    item.setQuantity(item.getQuantity() - quantity);
                }
            });
            this.items.removeIf(item -> item.getQuantity() <= 0);
        }
    }
}