package com.easysoft.finance.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Stock extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String exchange;

    @Column(nullable = true)
    private Long totalShare;

    // ... additional members, often include @OneToMany mappings
    public Stock() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public Stock(String name, String code) {
        this.name = name;
        this.symbol = code;
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

    public Long getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(Long totalShare) {
        this.totalShare = totalShare;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

}