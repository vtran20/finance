package com.easysoft.utils;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

@RestController
@EnableAutoConfiguration
public class Example {

//    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(Example.class, args);

        Stock stock = YahooFinance.get("CMP");

        BigDecimal price = stock.getQuote().getPrice();
        BigDecimal change = stock.getQuote().getChangeInPercent();
        BigDecimal peg = stock.getStats().getPeg();
        BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();

        System.out.println(stock.getName());
        System.out.println(stock.getStockExchange());
        System.out.println(price);
        System.out.println(change);
        System.out.println(peg);



//        stock.print();

//        Stock tesla = YahooFinance.get("TSLA", true);
//        System.out.println(tesla.getHistory());
    }
}
