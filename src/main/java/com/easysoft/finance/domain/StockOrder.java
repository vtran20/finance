package com.easysoft.finance.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class StockOrder extends BaseEntity implements Serializable {

    @Column(nullable = false)
    Date createdDate;
    @Column(nullable = false)
    Date updatedDate;
    @Column(nullable = true)
    Date settlementDate;
    @Column(nullable = false)
    private String symbol;

    @Column(nullable = true)
    private Long buyNum = 0l;
    @Column(nullable = true)
    private float buyFee = 0;
    @Column(nullable = true)
    private float buyPrice = 0;

    @Column(nullable = true)
    private Long sellNum = 0l;
    @Column(nullable = true)
    private float sellFee = 0;
    @Column(nullable = true)
    private float sellPrice = 0;

    @Column(nullable = true, length = 1)
    private String sent = "N";

    // ... additional members, often include @OneToMany mappings

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