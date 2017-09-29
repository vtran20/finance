package com.easysoft.finance.service.impl;

import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.finance.repository.StockDailyPriceRepository;
import com.easysoft.finance.repository.StockRepository;
import com.easysoft.finance.service.PriceService;
import com.easysoft.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by vutran on 9/25/2017.
 */
@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    StockDailyPriceRepository stockDailyPriceRepository;
    @Autowired
    StockRepository stockRepository;

    @Override
    public void importStockPrice(Stock stock) {
        double price = stock.getQuote().getPrice().doubleValue();
        Date date = Utils.getCurrentDateForStock(stock.getQuote().getLastTradeTime().getTime());
        StockDailyPrice stockDailyPrice = stockDailyPriceRepository.findBySymbolAnDate(stock.getSymbol(), date);
        if (stockDailyPrice == null) {
            stockDailyPrice = new StockDailyPrice();
            stockDailyPrice.setSymbol(stock.getSymbol());
            stockDailyPrice.setPrice(price);
            stockDailyPrice.setDate(date);
        } else {
            stockDailyPrice.setPrice(price);
        }
        stockDailyPriceRepository.save(stockDailyPrice);

    }

    @Override
    public void importStockPrice(Map<String, Stock> stocks) {
        if (stocks != null) {
            for (Map.Entry<String, yahoofinance.Stock> entry : stocks.entrySet())
            {
                importStockPrice(entry.getValue());
            }
        }

    }


    /**
     * Import history stock daily price before numberOfDay to the current day
     *
     * @param numberOfDay
     */
    public void importHistoryPrice (int numberOfDay) {
        try {
            for (com.easysoft.finance.domain.Stock st : stockRepository.findAll()) {
                Calendar calendar = Utils.getCalendarWithoutTime();
                calendar.add(Calendar.DAY_OF_YEAR, -numberOfDay);
                yahoofinance.Stock stock = YahooFinance.get(st.getSymbol(), calendar, Interval.DAILY);
                for (HistoricalQuote quote : stock.getHistory()) {
                    Date date = Utils.getCurrentDateForStock(quote.getDate().getTime());
                    StockDailyPrice stockDailyPrice = stockDailyPriceRepository.findBySymbolAnDate(stock.getSymbol(), date);
                    if (stockDailyPrice == null) {
                        stockDailyPrice = new StockDailyPrice();
                        stockDailyPrice.setSymbol(stock.getSymbol());
                        stockDailyPrice.setPrice(quote.getClose().doubleValue());
                        stockDailyPrice.setDate(date);
                    } else {
                        stockDailyPrice.setPrice(quote.getClose().doubleValue());
                    }
                    stockDailyPriceRepository.save(stockDailyPrice);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Import history stock daily price before numberOfDay to the current day for specific Stock
     *
     * @param numberOfDay
     * @param symbol
     */
    public void importHistoryPrice (int numberOfDay, String symbol) {
        try {
            Calendar calendar = Utils.getCalendarWithoutTime();
            calendar.add(Calendar.DAY_OF_YEAR, -numberOfDay);
            yahoofinance.Stock stock = YahooFinance.get(symbol, calendar, Interval.DAILY);
            for (HistoricalQuote quote : stock.getHistory()) {
                Date date = Utils.getCurrentDateForStock(quote.getDate().getTime());
                StockDailyPrice stockDailyPrice = stockDailyPriceRepository.findBySymbolAnDate(stock.getSymbol(), date);
                if (stockDailyPrice == null) {
                    stockDailyPrice = new StockDailyPrice();
                    stockDailyPrice.setSymbol(stock.getSymbol());
                    stockDailyPrice.setPrice(quote.getClose().doubleValue());
                    stockDailyPrice.setDate(date);
                } else {
                    stockDailyPrice.setPrice(quote.getClose().doubleValue());
                }
                stockDailyPriceRepository.save(stockDailyPrice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Import history stock daily price from to.
     *
     * @param from
     * @param to
     */
    public void importHistoryPrice (Date from, Date to) {
        try {
            for (com.easysoft.finance.domain.Stock st : stockRepository.findAll()) {
                Calendar calendarFrom = Utils.getCalendarWithoutTime(from);
                Calendar calendarTo = Utils.getCalendarWithoutTime(to);

                yahoofinance.Stock stock = YahooFinance.get(st.getSymbol(), calendarFrom, calendarTo, Interval.DAILY);
                for (HistoricalQuote quote : stock.getHistory()) {
                    Date date = Utils.getCurrentDateForStock(quote.getDate().getTime());
                    StockDailyPrice stockDailyPrice = stockDailyPriceRepository.findBySymbolAnDate(stock.getSymbol(), date);
                    if (stockDailyPrice == null) {
                        stockDailyPrice = new StockDailyPrice();
                        stockDailyPrice.setSymbol(stock.getSymbol());
                        stockDailyPrice.setPrice(quote.getClose().doubleValue());
                        stockDailyPrice.setDate(date);
                    } else {
                        stockDailyPrice.setPrice(quote.getClose().doubleValue());
                    }
                    stockDailyPriceRepository.save(stockDailyPrice);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
