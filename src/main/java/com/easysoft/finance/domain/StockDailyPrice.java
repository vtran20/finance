package com.easysoft.finance.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class StockDailyPrice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = true)
    private Date date;

    @Column(nullable = true)
    private double price;

    // ... additional members, often include @OneToMany mappings
    public StockDailyPrice() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}