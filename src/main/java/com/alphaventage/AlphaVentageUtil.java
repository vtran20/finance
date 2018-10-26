package com.alphaventage;

import com.alphaventage.stock.StockBatch;
import com.alphaventage.stock.StockInfo;
import com.alphaventage.stock.StockPrice;
import com.easysoft.finance.domain.Stock;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlphaVentageUtil {

    public static String ALPHA_VENTAGE_URL = "https://www.alphavantage.co/query?";
    public static String API_KEY = "UBX2MT8XVJOMTE07";
    public static String TIME_SERIES_INTRADAY = "TIME_SERIES_INTRADAY";
    public static String TIME_SERIES_DAILY = "TIME_SERIES_DAILY";
    public static String BATCH_STOCK_QUOTES = "BATCH_STOCK_QUOTES";

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
        return getStockDaily(symbol, false);
    }

    /**
     * Get stock data
     *
     * @param symbol
     * @param full = {false=compact|true=full}, default is false
     * @return
     * @throws Exception
     */
    public static StockInfo getStockDaily(String symbol, boolean full) throws Exception {
        if (StringUtils.isEmpty(symbol)) {
            throw new Exception("Symbol is empty");
        }
        String outputSize = "compact";
        if (full) {
            outputSize = "full";
        }
        RestTemplate restTemplate = new RestTemplate();
        StockInfo stockInfo = restTemplate.getForObject(ALPHA_VENTAGE_URL + "function={function}&symbol={symbol}&apikey={apikey}&outputsize={outputSize}", StockInfo.class, TIME_SERIES_DAILY, symbol, API_KEY, outputSize);
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<String, StockPrice> stock = mapper.convertValue(stockInfo.getMap(), new TypeReference<Map<String, StockPrice>>() {});
        stockInfo.setMap(stock);
        stockInfo.setSymbol(symbol);
        return stockInfo;
    }

    /**
     * The batch stock quotes API enables the querying of multiple stock quotes with a single API request, updated realtime.
     * It may serve as a lightweight alternative to our core stock time series APIs above (which have richer content but are symbol-specific).
     *
     * @param symbols
     * @return
     * @throws Exception
     */
    public static List<StockBatch> getBatchStocks(String symbols) throws Exception {
        if (StringUtils.isEmpty(symbols)) {
            throw new Exception("Symbol is empty");
        }
        RestTemplate restTemplate = new RestTemplate();
        StockInfo stockInfo = restTemplate.getForObject(ALPHA_VENTAGE_URL + "function={function}&symbols={symbol}&apikey={apikey}", StockInfo.class, BATCH_STOCK_QUOTES, symbols, API_KEY);
        ObjectMapper mapper = new ObjectMapper();
        List<StockBatch> stocks = mapper.convertValue(stockInfo.getStockBatches(), new TypeReference<List<StockBatch>>() {});
        return stocks;
    }

    public static void main(String[] args) {
        try {
            //getBatchStocks("MSFT,FB,AAPL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
