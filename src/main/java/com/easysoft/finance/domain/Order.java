package com.easysoft.finance.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    private Long boughtShareNum;
    @Column(nullable = true)
    private float boughtShareFee;
    @Column(nullable = true)
    private float boughtShareAmount;

    @Column(nullable = true)
    private Long soldShareNum;
    @Column(nullable = true)
    private float soldShareFee;
    @Column(nullable = true)
    private float soldShareAmount;

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

    public Long getBoughtShareNum() {
        return boughtShareNum;
    }

    public void setBoughtShareNum(Long boughtShareNum) {
        this.boughtShareNum = boughtShareNum;
    }

    public float getBoughtShareFee() {
        return boughtShareFee;
    }

    public void setBoughtShareFee(float boughtShareFee) {
        this.boughtShareFee = boughtShareFee;
    }

    public float getBoughtShareAmount() {
        return boughtShareAmount;
    }

    public void setBoughtShareAmount(float boughtShareAmount) {
        this.boughtShareAmount = boughtShareAmount;
    }

    public Long getSoldShareNum() {
        return soldShareNum;
    }

    public void setSoldShareNum(Long soldShareNum) {
        this.soldShareNum = soldShareNum;
    }

    public float getSoldShareFee() {
        return soldShareFee;
    }

    public void setSoldShareFee(float soldShareFee) {
        this.soldShareFee = soldShareFee;
    }

    public float getSoldShareAmount() {
        return soldShareAmount;
    }

    public void setSoldShareAmount(float soldShareAmount) {
        this.soldShareAmount = soldShareAmount;
    }

}