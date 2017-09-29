package com.easysoft.finance.service;

import yahoofinance.Stock;

import java.util.Date;
import java.util.Map;

/**
 * Created by vutran on 9/25/2017.
 */
public interface PriceService {
    public void importStockPrice (Stock stock);
    public void importStockPrice (Map<String, Stock> stock);
    public void importHistoryPrice (int numberOfDay);
    public void importHistoryPrice (int numberOfDay, String symbol);
    public void importHistoryPrice (Date from, Date to);
}
