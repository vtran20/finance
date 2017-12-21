package com.easysoft.finance.service;

import com.alphaventage.stock.StockInfo;
import com.easysoft.finance.domain.StockAnalysisDuration;

import java.util.Date;
import java.util.List;

/**
 * Created by vutran on 9/25/2017.
 */
public interface PriceService {
    public void importStockPrice (String symbol);
    public void importStockPrice (List<String> symbols);
    public void importHistoryPrice (boolean full);
    public void importHistoryPrice (String symbol);
    public void importHistoryPrice (String symbol, boolean full);

    public void analysisStock(StockAnalysisDuration duration);
}
