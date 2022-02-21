package com.easysoft.finance.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Stock extends BaseEntity implements Serializable {

    @Column(nullable = false, unique = true)
    private String symbol;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String exchange;

    @Column(nullable = true)

    private Long volumns;

    // ... additional members, often include @OneToMany mappings
    public Stock() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public Stock(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getVolumns() {
        return volumns;
    }

    public void setVolumns(Long volumns) {
        this.volumns = volumns;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

}