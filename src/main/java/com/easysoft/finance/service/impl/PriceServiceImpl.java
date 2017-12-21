package com.easysoft.finance.service.impl;

import com.alphaventage.AlphaVentageUtil;
import com.alphaventage.stock.Base;
import com.alphaventage.stock.StockInfo;
import com.alphaventage.stock.StockPrice;
import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.finance.repository.StockDailyPriceRepository;
import com.easysoft.finance.repository.StockRepository;
import com.easysoft.finance.service.PriceService;
import com.easysoft.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by vutran on 9/25/2017.
 */
@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    StockDailyPriceRepository stockDailyPriceRepository;
    @Autowired
    StockRepository stockRepository;

    /**
     * Update stock price in current day
     *
     * @param symbol
     */
    @Override
    public void importStockPrice(String symbol) {
        StockInfo stockInfo = null;
        try {
            stockInfo = AlphaVentageUtil.getStockInTradeDay(symbol);
            double price = stockInfo.getCurrentPrice();
            Date date = stockInfo.getCurrentDate().getTime();
            StockDailyPrice stockDailyPrice = stockDailyPriceRepository.findBySymbolAnDate(stockInfo.getSymbol(), date);
            if (stockDailyPrice == null) {
                stockDailyPrice = new StockDailyPrice();
                stockDailyPrice.setSymbol(stockInfo.getSymbol());
                stockDailyPrice.setPrice(price);
                stockDailyPrice.setDate(date);
            } else {
                stockDailyPrice.setPrice(price);
            }
            stockDailyPriceRepository.save(stockDailyPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update list stock price in current day
     *
     * @param symbols
     */
    @Override
    public void importStockPrice(List<String> symbols) {
        if (symbols != null) {
            for (String symbol: symbols)
            {
                importStockPrice(symbol);
            }
        }

    }


    /**
     * Import history stock daily price before numberOfDay to the current day
     *
     */
    public void importHistoryPrice () {
        try {
            for (Stock st : stockRepository.findAll()) {
                importHistoryPrice(st.getSymbol());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Import history stock daily price before numberOfDay to the current day for specific Stock
     *
     * @param symbol
     */
    public void importHistoryPrice (String symbol) {
        importHistoryPrice(symbol, false);
    }
    public void importHistoryPrice (String symbol, boolean full) {
        try {
            if (StringUtils.isEmpty(symbol)) return;
            List <StockDailyPrice>stockDailyPrices = new ArrayList<StockDailyPrice>();
            StockInfo stockInfo = AlphaVentageUtil.getStockDaily(symbol, full);
            for (Map.Entry<String, StockPrice> quote : stockInfo.getMap().entrySet()) {
                Date date = Utils.getCalendarWithoutTime(new SimpleDateFormat(Base.DATE_FORMAT).parse(quote.getKey())).getTime();
                StockDailyPrice stockDailyPrice = stockDailyPriceRepository.findBySymbolAnDate(stockInfo.getSymbol(), date);
                if (stockDailyPrice == null) {
                    stockDailyPrice = new StockDailyPrice();
                    stockDailyPrice.setSymbol(stockInfo.getSymbol());
                    stockDailyPrice.setPrice(quote.getValue().getClosePrice());
                    stockDailyPrice.setDate(date);
                    stockDailyPrices.add(stockDailyPrice);
                } else {
                    stockDailyPrice.setPrice(quote.getValue().getClosePrice());
                    stockDailyPrices.add(stockDailyPrice);
                }
            }
            if (!stockDailyPrices.isEmpty()) {
                stockDailyPriceRepository.saveAll(stockDailyPrices);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
