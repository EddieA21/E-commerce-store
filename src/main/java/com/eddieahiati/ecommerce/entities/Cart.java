package com.eddieahiati.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
public class Cart {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date_created")
    private Date dateCreated = new Date();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public BigDecimal overAllTotal() {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for(var item : cartItems) {
            totalPrice = totalPrice.add(item.getTotalPrice());
        }

        return totalPrice;
    }
}
