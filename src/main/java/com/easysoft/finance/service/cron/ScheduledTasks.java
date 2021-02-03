package com.easysoft.finance.service.cron;

import com.alphaventage.AlphaVentageUtil;
import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.finance.domain.StockOrder;
import com.easysoft.finance.repository.StockOrderRepository;
import com.easysoft.finance.repository.StockDailyPriceRepository;
import com.easysoft.finance.repository.StockRepository;
import com.easysoft.finance.service.EmailService;
import com.easysoft.finance.service.PriceService;
import com.easysoft.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    StockOrderRepository stockOrderRepository;
    @Autowired
    StockDailyPriceRepository stockDailyPriceRepository;
    @Autowired
    PriceService priceService;
    @Autowired
    EmailService emailService;

    /**
     * This method will be run at 9:30 every morning and reset all data need.
     */
    @Scheduled(cron = "0 30 9 * * *") //EST
//    @Scheduled(cron = "0 30 8 * * *") //EDT
//    @Scheduled(cron = "0 30 3 * * *") //UTC
    public void resetDataDaily() {
        //Reset keyMap data.

        //Reset totalRequestCountUsed=0
        AlphaVentageUtil.totalRequestCountUsed = 0;
    }

    /**
     * This method will get price from Alpha Vantage and update into each stock on each day. Data of Saturday or Sunday will be stored in last Friday.
     */
    @Scheduled(cron = "0 5 10,12,14 * * MON-FRI") //EST
//    @Scheduled(cron = "0 5 9,11,13 * * MON-FRI") //EDT
//    @Scheduled(cron = "0 5 14,16,18 * * MON-FRI") //UTC
    public void importDailyPrice() {

        //get all stocks
        List<String> ls = new ArrayList<>();
        for (Iterator<Stock> i = stockRepository.findAll().iterator(); i.hasNext(); ) {
            ls.add(i.next().getSymbol());
        }
        priceService.importStockPrice(ls);
        log.info("Import daily Price " + new Date());
        notifyForStockCanBeSold();
    }

    /**
     * This method will get price from Alpha Vantage and update into each stock at 1st each month. Data of Saturday or Sunday will be stored in last Friday.
     */
//    @Scheduled(cron = "0 5 17 * * MON-FRI") //EST
//    @Scheduled(cron = "0 5 16 * * MON-FRI") //EDT
//    @Scheduled(cron = "0 5 21 * * MON-FRI") //UTC
    @Scheduled(cron = "0 20 14 * * MON-FRI")
    public void importMonthlyPrice() {
        priceService.importHistoryPrice(false);
        log.info("Import Daily Last Price: " + new Date());
    }

    /**
     * Send email to notify stocks have interest. This method will be called after update stock price
     */
//    @Scheduled(cron = "0 */11 8-17 * * MON-FRI")
    public void notifyForStockCanBeSold() {
        List<StockOrder> stockOrders = stockOrderRepository.findActiveOrders();
        for (StockOrder order : stockOrders) {
            StockDailyPrice stockDailyPrice = stockDailyPriceRepository.findBySymbolAnDate(order.getSymbol(), Utils.getCurrentDateForStock());
            if (stockDailyPrice != null) {
                double currentPrice = stockDailyPrice.getPrice() * (order.getBuyNum() - order.getSellNum());
                double orderPrice = order.getBuyPrice() * (order.getBuyNum() - order.getSellNum());

                double interest = ((currentPrice - orderPrice) * 100) / currentPrice;
                log.info(interest);
                if (interest > 3) {
                    emailService.sendTextMail("SS", "SS - " + order.getSymbol() + ":" + interest);
                    order.setSent("Y");
                    stockOrderRepository.save(order);
                }
            }
        }
    }

//    /*When we load price for a symbol, we add in here so that we will not process it again until need*/
//    private static List <String>processedSymbols = new ArrayList<String>();
//    /* This variable is used to clear processedSymbols.
//     * When the morning process run, it will check this variable,
//     * 1. if isMorning is true {
//      *     run until no symbol remain. If no symbol remain and isMorning=true do nothing.
//      *   } else {
//      *     isMorning = false;
//      *     processedSymbols.clear()
//      *   }
//      *
//      *   When the evening process run, it will check this variable,
//     * 2. if isMorning is false {
//      *     run until no symbol remain. If no symbol remain and isMorning=true do nothing.
//      *   } else {
//      *     isMorning = true;
//      *     processedSymbols.clear()
//      *   }
//     */
//    private static boolean isMorning = true;
//    private static boolean noRemain = false;
//    private static int REQUEST_PER_MINUTE = 5;
//    /**
//     * This method will get price from Alpha Vantage and update into each stock on each day. Data of Saturday or Sunday will be stored in last Friday.
//     */
//    @Scheduled(cron = "0 */2 10-16 * * MON-FRI")
//    public void importDailyPriceMorning() {
//
//        //get all stocks
//        if (isMorning) {
//            if (noRemain) return;
//            List<String> ls = new ArrayList<>();
//            String symbol;
//            for (Iterator<Stock> i = stockRepository.findAll().iterator(); i.hasNext();) {
//                symbol = i.next().getSymbol();
//                if (!processedSymbols.contains(symbol)) {
//                    ls.add(symbol);
//                    if (ls.size() >= REQUEST_PER_MINUTE) break; //Alpha Vantage only support 5 request per minutes and 500 per day
//                }
//            }
//            if (ls.size() == 0) {
//                noRemain = true;
//            } else {
//                priceService.importStockPrice(ls, processedSymbols);
//            }
//            log.info("Import daily Price " + new Date());
//            System.out.println("Import daily Price morning " + new Date() + " isMorning:" +isMorning + " noRemain:"+noRemain);
//        } else {
//            //prepare for morning run
//            isMorning = true;
//            noRemain = false;
//            processedSymbols.clear();
//        }
//    }
//
//    @Scheduled(cron = "0 */2 17-23 * * MON-FRI")
//    public void importDailyPriceEvening() {
//
//        //get all stocks
//        if (!isMorning) {
//            if (noRemain) return;
//            List<String> ls = new ArrayList<>();
//            String symbol;
//            for (Iterator<Stock> i = stockRepository.findAll().iterator(); i.hasNext();) {
//                symbol = i.next().getSymbol();
//                if (!processedSymbols.contains(symbol)) {
//                    ls.add(symbol);
//                    processedSymbols.add(symbol);
//                    if (ls.size() >= REQUEST_PER_MINUTE) break; //Alpha Vantage only support 5 request per minutes and 500 per day
//                }
//            }
//            if (ls.size() == 0) {
//                noRemain = true;
//            } else {
//                priceService.importStockPrice(ls, processedSymbols);
//            }
//            log.info("Import daily Price " + new Date());
//            System.out.println("Import daily Price evening " + new Date() + " isMorning:" +isMorning + " noRemain:"+noRemain);
//        } else {
//            //prepare for evening run
//            isMorning = false;
//            noRemain = false;
//            processedSymbols.clear();
//        }
//    }

}
