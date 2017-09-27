package com.easysoft.finance.component;

import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.repository.StockRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private StockRepository stockRepository;

    private Logger log = Logger.getLogger(DataLoader.class);

    @Autowired
    public void setStockRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Stock stock = new Stock();
        stock.setName("Apple");
        stock.setSymbol("AAPL");
        stockRepository.save(stock);


        stock = new Stock();
        stock.setName("TESLA");
        stock.setSymbol("TSLA");
        stockRepository.save(stock);

    }
}
