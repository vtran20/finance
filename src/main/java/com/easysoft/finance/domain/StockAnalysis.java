package com.easysoft.finance.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class StockAnalysis extends BaseEntity implements Serializable {

    @Column(nullable = true)
    private String symbol;
    @Column(nullable = true)
    private double cumulativeReturn;
    @Column(nullable = true)
    private double averageReturn;
    @Column(nullable = true)
    private double riskStandardDeviation;
    @Column(nullable = true)
    private double sharpeRatio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_analysis_duration_id")
    private StockAnalysisDuration stockAnalysisDuration;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getCumulativeReturn() {
        return cumulativeReturn;
    }

    public void setCumulativeReturn(double cumulativeReturn) {
        this.cumulativeReturn = cumulativeReturn;
    }

    public double getAverageReturn() {
        return averageReturn;
    }

    public void setAverageReturn(double averageReturn) {
        this.averageReturn = averageReturn;
    }

    public double getRiskStandardDeviation() {
        return riskStandardDeviation;
    }

    public void setRiskStandardDeviation(double riskStandardDeviation) {
        this.riskStandardDeviation = riskStandardDeviation;
    }

    public double getSharpeRatio() {
        return sharpeRatio;
    }

    public void setSharpeRatio(double sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    public StockAnalysisDuration getStockAnalysisDuration() {
        return stockAnalysisDuration;
    }

    public void setStockAnalysisDuration(StockAnalysisDuration stockAnalysisDuration) {
        this.stockAnalysisDuration = stockAnalysisDuration;
    }
}