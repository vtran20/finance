package com.easysoft.finance.service.cron;

import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.finance.domain.StockOrder;
import com.easysoft.finance.repository.StockOrderRepository;
import com.easysoft.finance.repository.StockDailyPriceRepository;
import com.easysoft.finance.repository.StockRepository;
import com.easysoft.finance.service.EmailService;
import com.easysoft.finance.service.PriceService;
import com.easysoft.utils.Utils;
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
     * This method will get price from Alpha Vantage and update into each stock on each day. Data of Saturday or Sunday will be stored in last Friday.
     */
    @Scheduled(cron = "0 0 8-18 * * ?")
    public void importDailyPrice() {

        //get all stocks
        List<String> ls = new ArrayList<>();
        for (Iterator<Stock> i = stockRepository.findAll().iterator(); i.hasNext();) {
            ls.add(i.next().getSymbol());
        }
        priceService.importStockPrice(ls);
        log.info("Import daily Price " + new Date());
    }

    /**
     * This method will get price from Alpha Vantage and update into each stock at 1st each month. Data of Saturday or Sunday will be stored in last Friday.
     *
     */
    @Scheduled(cron = "0 0 18 1 * ?")
    public void importMonthlyPrice() {
        priceService.importHistoryPrice(false);
        log.info("Import monthly Price: " + new Date());
    }

    /**
     * Send email to notify stocks have interest.
     */
    @Scheduled(cron = "0 */11 8-17 * * ?")
    public void notifyForStockCanBeSold() {
        List<StockOrder> stockOrders = stockOrderRepository.findActiveOrders();
        for (StockOrder order: stockOrders) {
            StockDailyPrice stockDailyPrice = stockDailyPriceRepository.findBySymbolAnDate(order.getSymbol(), Utils.getCurrentDateForStock());
            if (stockDailyPrice != null) {
                double currentPrice = stockDailyPrice.getPrice()*(order.getBuyNum() - order.getSellNum());
                double orderPrice = order.getBuyPrice()*(order.getBuyNum() - order.getSellNum());

                double interest = ((currentPrice - orderPrice)*100)/currentPrice;
                log.info(interest);
                if (interest > 3) {
                    emailService.sendTextMail("SS", "SS - "+order.getSymbol() + ":"+interest);
                    order.setSent("Y");
                    stockOrderRepository.save(order);
                }
            }
        }
    }
}
