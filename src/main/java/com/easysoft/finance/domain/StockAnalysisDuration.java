package com.easysoft.finance.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class StockAnalysisDuration extends BaseEntity implements Serializable {

    @Column(nullable = true)
    private String name;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date endDate;

    @OneToMany(mappedBy = "stockAnalysisDuration",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    List<StockAnalysis> stockAnalysisList = new ArrayList<StockAnalysis>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<StockAnalysis> getStockAnalysisList() {
        return stockAnalysisList;
    }

    public void setStockAnalysisList(List<StockAnalysis> stockAnalysisList) {
        this.stockAnalysisList = stockAnalysisList;
    }
}