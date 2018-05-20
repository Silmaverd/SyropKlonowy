package com.blinenterprise.SyropKlonowy.domain.Delivery;


import com.blinenterprise.SyropKlonowy.domain.Product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class ProductWithQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    private Product product;

    private int quantity;

    public ProductWithQuantity(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public boolean decreaseAmountBy(int amount) {
        if (this.quantity >= amount) {
            this.quantity -= amount;
            return true;
        } else return false;
    }

    public void increaseAmountBy(int amount) {
        this.quantity += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductWithQuantity that = (ProductWithQuantity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
