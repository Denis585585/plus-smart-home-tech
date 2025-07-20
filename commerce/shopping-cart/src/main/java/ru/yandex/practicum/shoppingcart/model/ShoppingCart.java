package ru.yandex.practicum.shoppingcart.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "shopping_cart")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shoppingCartId;
    private String username;
    private Boolean active;
    @ElementCollection
    @CollectionTable(name = "shopping_cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products = new HashMap<>();
}