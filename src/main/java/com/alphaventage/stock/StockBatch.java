package com.alphaventage.stock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class StockBatch {
    @JsonProperty("1. symbol")
    private String symbol;
    @JsonProperty("2. price")
    private double price;
    @JsonProperty("3. volume")
    private long volume;
    @JsonProperty("4. timestamp")
    private String date;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @JsonCreator
    public StockBatch(Map<String, Object> delegate) {
        symbol = (String) delegate.get("1. symbol");
        price = Double.valueOf((String) delegate.get("2. price"));
        volume = Long.valueOf((String) delegate.get("3. volume"));
        date = (String) delegate.get("4. timestamp");

    }
}
