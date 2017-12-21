package com.easysoft.utils;

import com.easysoft.finance.repository.StockDailyPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Example {

//    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }
    @Autowired
    private StockDailyPriceRepository stockDailyPriceRepository;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);



//        stock.print();

//        Stock tesla = YahooFinance.get("TSLA", true);
//        System.out.println(tesla.getHistory());
    }
}
