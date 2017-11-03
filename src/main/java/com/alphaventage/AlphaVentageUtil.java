package com.alphaventage;

import com.alphaventage.stock.StockInfo;
import com.alphaventage.stock.StockPrice;
import com.easysoft.finance.domain.Stock;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

public class AlphaVentageUtil {

    public static String ALPHA_VENTAGE_URL = "https://www.alphavantage.co/query?";
    public static String API_KEY = "UBX2MT8XVJOMTE07";
    public static String TIME_SERIES_INTRADAY = "TIME_SERIES_INTRADAY";
    public static String TIME_SERIES_DAILY = "TIME_SERIES_DAILY";

    public static StockInfo getStockInTradeDay(String symbol) throws Exception {
        return getStockInTradeDay(symbol, null);
    }
    public static StockInfo getStockInTradeDay(String symbol, String interval) throws Exception {
        if (StringUtils.isEmpty(symbol)) {
            throw new Exception("Symbol is empty");
        }
        if (StringUtils.isEmpty(interval)) {
            interval = "1min";
        }
        RestTemplate restTemplate = new RestTemplate();
        StockInfo stockInfo = restTemplate.getForObject(ALPHA_VENTAGE_URL + "function={function}&symbol={symbol}&interval={interval}&apikey={apikey}", StockInfo.class, TIME_SERIES_INTRADAY, symbol, interval, API_KEY);
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<String, StockPrice> stock = mapper.convertValue(stockInfo.getMap(), new TypeReference<Map<String, StockPrice>>() {});
        stockInfo.setMap(stock);
        stockInfo.setSymbol(symbol);
        return stockInfo;
    }


    public static StockInfo getStockDaily(String symbol) throws Exception {
        if (StringUtils.isEmpty(symbol)) {
            throw new Exception("Symbol is empty");
        }
        RestTemplate restTemplate = new RestTemplate();
        StockInfo stockInfo = restTemplate.getForObject(ALPHA_VENTAGE_URL + "function={function}&symbol={symbol}&apikey={apikey}", StockInfo.class, TIME_SERIES_DAILY, symbol, API_KEY);
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<String, StockPrice> stock = mapper.convertValue(stockInfo.getMap(), new TypeReference<Map<String, StockPrice>>() {});
        stockInfo.setMap(stock);
        stockInfo.setSymbol(symbol);
        return stockInfo;
    }

}
