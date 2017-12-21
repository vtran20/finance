package com.easysoft.finance.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class StockAnalysis extends BaseEntity implements Serializable {

    @Column(nullable = true)
    private String symbol;
    @Column(nullable = true)
    private float cumulativeReturn;
    @Column(nullable = true)
    private float averageReturn;
    @Column(nullable = true)
    private float riskStandardDeviation;
    @Column(nullable = true)
    private float sharpeRatio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_analysis_duration_id")
    private StockAnalysisDuration stockAnalysisDuration;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getCumulativeReturn() {
        return cumulativeReturn;
    }

    public void setCumulativeReturn(float cumulativeReturn) {
        this.cumulativeReturn = cumulativeReturn;
    }

    public float getAverageReturn() {
        return averageReturn;
    }

    public void setAverageReturn(float averageReturn) {
        this.averageReturn = averageReturn;
    }

    public float getRiskStandardDeviation() {
        return riskStandardDeviation;
    }

    public void setRiskStandardDeviation(float riskStandardDeviation) {
        this.riskStandardDeviation = riskStandardDeviation;
    }

    public float getSharpeRatio() {
        return sharpeRatio;
    }

    public void setSharpeRatio(float sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    public StockAnalysisDuration getStockAnalysisDuration() {
        return stockAnalysisDuration;
    }

    public void setStockAnalysisDuration(StockAnalysisDuration stockAnalysisDuration) {
        this.stockAnalysisDuration = stockAnalysisDuration;
    }
}