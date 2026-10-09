package com.estibenjack.coffeepos.ingredient;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private MeasurementUnit unit;

    @Column(
            name="stock_quantity",
            nullable=false,
            precision=12,
            scale=3
    )
    private BigDecimal stockQuantity;

    protected Ingredient() {
    }

    public Ingredient(String name, MeasurementUnit unit){
        if(name==null || name.isBlank()){
            throw new IllegalArgumentException(
                    "Ingredient name is required"
            );
        }
        if(unit==null){
            throw new IllegalArgumentException(
                    "Measurement unit is required"
            );
        }

        this.name=name.trim();
        this.unit=unit;
        this.stockQuantity=BigDecimal.ZERO;
    }

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public MeasurementUnit getUnit(){
        return unit;
    }

    public BigDecimal getStockQuantity(){
        return stockQuantity;
    }

    public void replenishStock(BigDecimal quantity){
        validatePositiveQuantity(quantity);
        this.stockQuantity=this.stockQuantity.add(quantity);
    }

    private void validatePositiveQuantity(BigDecimal quantity){
        if(quantity==null || quantity.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    public void consumeStock(BigDecimal quantity){
        validatePositiveQuantity(quantity);
        if(this.stockQuantity.compareTo(quantity)<0){
            throw new IllegalStateException("Insufficient stock for ingredient: "+name);
        }
        this.stockQuantity=this.stockQuantity.subtract(quantity);
    }

}
