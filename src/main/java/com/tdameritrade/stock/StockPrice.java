package com.tdameritrade.stock;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class StockPrice {
    @JsonProperty("1. open")
    private double openPrice;
    @JsonProperty("2. high")
    private double highPrice;
    @JsonProperty("3. low")
    private double lowPrice;
    @JsonProperty("4. close")
    private double closePrice;
    @JsonProperty("5. volume")
    private long volume;

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @JsonCreator
    public StockPrice (Map<String,Object> delegate) {
        openPrice = Double.valueOf((String) delegate.get("1. open"));
        highPrice = Double.valueOf((String) delegate.get("2. high"));
        lowPrice = Double.valueOf((String) delegate.get("3. low"));
        closePrice = Double.valueOf((String) delegate.get("4. close"));
        volume = Long.valueOf((String) delegate.get("5. volume"));

    }
}
