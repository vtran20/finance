package com.alphaventage;

import com.alphaventage.stock.StockBatch;
import com.alphaventage.stock.StockInfo;
import com.alphaventage.stock.StockPrice;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AlphaVentageUtil {

    public static String ALPHA_VENTAGE_URL = "https://www.alphavantage.co/query?";
    public static String[] API_KEY = {"P367S5GY5FFVL6CB"/*,"9T8D6Y4HL4XFNBJW","UBX2MT8XVJOMTE07", "ZOD2DP7OAN1S7WI9", "ZGPF0CBUSTLTBC87","CUO5H1ZYAKJU8WAM"*/};//vuktx@yahoo.com, 1979, oracle
    public static Integer totalRequestCount = 500*API_KEY.length;
    public static Integer totalRequestCountUsed = 0;
    public static String TIME_SERIES_INTRADAY = "TIME_SERIES_INTRADAY";
    public static String TIME_SERIES_DAILY = "TIME_SERIES_DAILY";
    public static String BATCH_STOCK_QUOTES = "BATCH_STOCK_QUOTES";

    private static Map <String, Integer> apiKeyMap = Collections.synchronizedMap(new HashMap<String, Integer>());
    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH:mm");

    /**
     * Return an available key for request. If don't have, waiting for a minutes and try again.
     *
     * Map will store number of time the each key already used for request and based on that we will move to the next available key or wait for a next minute.
     *
     * map = {
     *     key1_time: number of time was used for request,
     *     key2_time: number of time was used for request
     * }
     *
     * @return
     */
    public static String getAPIKey () {
        String key = null;
        Calendar calendar = Calendar.getInstance();
        String dateTime = dateFormat.format(calendar.getTime());
        for (String s : API_KEY) {
            String keyMap = s+"_"+dateTime;
            if (apiKeyMap.get(keyMap) != null) {
                Integer count = apiKeyMap.get(keyMap);
                Integer MAX_REQUEST_MIN = 5;
                if (count < MAX_REQUEST_MIN) {
                    key = s;
                    apiKeyMap.put(keyMap, count + 1);
                    totalRequestCountUsed++;
                    break;
                } else {
                    //the previous key was full of service, move to the next one
                    continue;
                }
            } else {
                key = s;
                apiKeyMap.put(keyMap, 1);
                totalRequestCountUsed++;
                break;
            }
        }
        if (key == null) {
            printMap();
            try {
                System.out.println("Start sleep 1 minute");
                Thread.sleep(60000);//1 minute
                System.out.println("End sleep 1 minute");
                apiKeyMap.clear();
                return getAPIKey();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Calendar.getInstance().getTime()+"="+key+"_"+dateTime);
        return key;
    }

    private static void printMap() {
        //print map
        for (String stg: apiKeyMap.keySet()) {
            System.out.println(stg +":"+apiKeyMap.get(stg));
        }
        System.out.println("---------");
    }

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
        StockInfo stockInfo = restTemplate.getForObject(ALPHA_VENTAGE_URL + "function={function}&symbol={symbol}&interval={interval}&apikey={apikey}", StockInfo.class, TIME_SERIES_INTRADAY, symbol, interval, getAPIKey());
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
        StockInfo stockInfo = restTemplate.getForObject(ALPHA_VENTAGE_URL + "function={function}&symbol={symbol}&apikey={apikey}&outputsize={outputSize}", StockInfo.class, TIME_SERIES_DAILY, symbol, getAPIKey(), outputSize);
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
        StockInfo stockInfo = restTemplate.getForObject(ALPHA_VENTAGE_URL + "function={function}&symbols={symbol}&apikey={apikey}", StockInfo.class, BATCH_STOCK_QUOTES, symbols, getAPIKey());
        ObjectMapper mapper = new ObjectMapper();
        List<StockBatch> stocks = mapper.convertValue(stockInfo.getStockBatches(), new TypeReference<List<StockBatch>>() {});
        return stocks;
    }

    public static void main(String[] args) {
        try {
            for (int i = 0; i < 31; i++) {
                System.out.println(getAPIKey());
            }
            printMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
