package com.tdameritrade.stock;

import com.easysoft.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

//@JsonIgnoreProperties(ignoreUnknown = true)

/**
 * If want to create an object, remove extends Base and declare all proporties
 */
public class StockInfo extends Base {

    @JsonProperty(value="symbol")
    private String symbol;

    public String getSymbol() {
        return symbol;
    }


    //    private String symbol;
//
//    private Map stock;

//    public String getSymbol() {
//        return symbol;
//    }
//
//    public void setSymbol(String symbol) {
//        this.symbol = symbol;
//    }
//
//    @JsonProperty("Meta Data") private MetaInfo metaInfo;
//    public MetaInfo getMetaInfo() {
//        return metaInfo;
//    }
//
//    public void setMetaInfo(MetaInfo metaInfo) {
//        this.metaInfo = metaInfo;
//    }
//
//    //@JsonProperty("Time Series (1min)")
//    private Map<String, StockPrice> map;
//    public Map <String, StockPrice> getMap() {
//        return map;
//    }
//
//    @JsonSetter("Time Series (1min)")
//    public void setMap(Map map) {
//        this.map = map;
//    }
//    @JsonSetter("Time Series (Daily)")
//    public void setMapDaily(Map map) {
//        this.map = map;
//    }
//
//    private List<StockBatch> stockBatches;
//
//    public List<StockBatch> getStockBatches() {
//        return stockBatches;
//    }
//    @JsonSetter("Stock Quotes")
//    public void setMapStockBatch(List stockBatches) {
//        this.stockBatches = stockBatches;
//    }
//
//
//    @Override
//    public String toString() {
//        return "Value{" +
//                "Symbol=" + metaInfo.getSymbol() +
//                ", quote='" + metaInfo.getLastRefreshed() + '\'' +
//                ", list='" + map + '\'' +
//                '}';
//    }
//
//    public double getCurrentPrice() {
//        if (map != null && !map.isEmpty() && metaInfo.getInterval().contains("min")) {
//            Map.Entry<String, StockPrice> stockInfo =  map.entrySet().iterator().next();
//            return stockInfo.getValue().getClosePrice();
//        }
//        return -1d;
//    }
//    public Calendar getCurrentDate() {
//        Calendar calendar = null;
//        if (map != null && !map.isEmpty() && metaInfo.getInterval().contains("min")) {
//            Map.Entry<String, StockPrice> stockInfo =  map.entrySet().iterator().next();
//            if (stockInfo.getKey() != null) {
//                try {
//                    calendar = Utils.getCalendarWithoutTime(new SimpleDateFormat(DATETIME_FORMAT).parse(stockInfo.getKey()));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        return calendar;
//    }

}
