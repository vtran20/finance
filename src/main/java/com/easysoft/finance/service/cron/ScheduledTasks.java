package com.easysoft.finance.service.cron;

import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.finance.domain.pojo.StockPriceHistory;
import com.easysoft.finance.repository.OrderRepository;
import com.easysoft.finance.repository.StockDailyPriceRepository;
import com.easysoft.finance.repository.StockRepository;
import com.easysoft.finance.service.EmailService;
import com.easysoft.finance.service.PriceService;
import com.easysoft.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by vutran on 9/22/2017.
 */

@Component
public class ScheduledTasks {

    private Logger log = Logger.getLogger(ScheduledTasks.class);

    @Autowired
    StockRepository stockRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    StockDailyPriceRepository stockDailyPriceRepository;
    @Autowired
    PriceService priceService;
    @Autowired
    EmailService emailService;

    /**
     * This method will get price from Yahoo Finance and update into each stock on each day. Data of Saturday or Sunday will be stored in last Friday.
     */
    @Scheduled(cron = "0 */5 0-23 * * ?")
    public void importDailyPrice() {

        //get all stocks
        List<String> ls = new ArrayList<>();
        for (Iterator<Stock> i = stockRepository.findAll().iterator(); i.hasNext();) {
            ls.add(i.next().getSymbol());
        }
        String[] stockArr = new String[ls.size()];
        //build symbol array
        stockArr = ls.toArray(stockArr);
        //get prices from symbol array
        Map<String, yahoofinance.Stock> stocks = null;
        try {
            stocks = YahooFinance.get(stockArr, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        priceService.importStockPrice(stocks);
        log.info("The time is now 1 " + new Date());
    }

    /**
     * This method will get price from Yahoo Finance and update into each stock on each day. Data of Saturday or Sunday will be stored in last Friday.
     */
    @Scheduled(cron = "0 5 0-23 * * ?")
    public void notifyForStockCanBeSold() {

        emailService.sendTextMail("test", "test");
    }
}
