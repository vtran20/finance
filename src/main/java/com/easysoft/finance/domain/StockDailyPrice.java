package com.easysoft.finance.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(indexes = {
        @Index(columnList = "symbol")
//        @Index(name = "fn_index", columnList = "firstName"),
//        @Index(name = "mulitIndex1", columnList = "firstName, lastName"),
//        @Index(name = "mulitIndex2", columnList = "lastName, firstName"),
//        @Index(name = "mulitSortIndex", columnList = "firstName, lastName DESC"),
//        @Index(name = "uniqueIndex", columnList = "firstName", unique = true),
//        @Index(name = "uniqueMulitIndex", columnList = "firstName, lastName", unique = true)
})
public class StockDailyPrice extends BaseEntity implements Serializable {
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