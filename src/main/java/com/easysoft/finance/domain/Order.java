package com.easysoft.finance.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity (name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    Date createdDate;
    @Column(nullable = false)
    Date updatedDate;
    @Column(nullable = true)
    Date settlementDate;
    @Column(nullable = false)
    private String symbol;

    @Column(nullable = true)
    private Long buyNum;
    @Column(nullable = true)
    private float buyFee;
    @Column(nullable = true)
    private float buyPrice;

    @Column(nullable = true)
    private Long sellNum;
    @Column(nullable = true)
    private float sellFee;
    @Column(nullable = true)
    private float sellPrice;

    @Column(nullable = true, length = 1)
    private String sent;

    // ... additional members, often include @OneToMany mappings

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Long buyNum) {
        this.buyNum = buyNum;
    }

    public float getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(float buyFee) {
        this.buyFee = buyFee;
    }

    public float getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(float buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Long getSellNum() {
        return sellNum;
    }

    public void setSellNum(Long sellNum) {
        this.sellNum = sellNum;
    }

    public float getSellFee() {
        return sellFee;
    }

    public void setSellFee(float sellFee) {
        this.sellFee = sellFee;
    }

    public float getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(float sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }
}