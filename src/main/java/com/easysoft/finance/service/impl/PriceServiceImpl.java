package com.easysoft.finance.service.impl;

import com.alphaventage.AlphaVentageUtil;
import com.alphaventage.stock.Base;
import com.alphaventage.stock.StockInfo;
import com.alphaventage.stock.StockPrice;
import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.domain.StockAnalysis;
import com.easysoft.finance.domain.StockAnalysisDuration;
import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.finance.domain.pojo.SharpeRatio;
import com.easysoft.finance.repository.StockAnalysisDurationRepository;
import com.easysoft.finance.repository.StockAnalysisRepository;
import com.easysoft.finance.repository.StockDailyPriceRepository;
import com.easysoft.finance.repository.StockRepository;
import com.easysoft.finance.service.PriceService;
import com.easysoft.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PriceServiceImpl implements PriceService {
    private Logger log = LoggerFactory.getLogger(PriceServiceImpl.class);


    @Autowired
    StockDailyPriceRepository stockDailyPriceRepository;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    StockAnalysisRepository stockAnalysisRepository;
    @Autowired
    StockAnalysisDurationRepository stockAnalysisDurationRepository;

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
            log.info("importStockPrice success for share:"+symbol);
        } catch (Exception e) {
            log.info("importStockPrice fail for share:"+symbol);
//            e.printStackTrace();
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
    public void importHistoryPrice (boolean full) {
        try {
            for (Stock st : stockRepository.findAll()) {
                importHistoryPrice(st.getSymbol(), full);
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
            log.info("importHistoryPrice fail for share:"+symbol);
            e.printStackTrace();
        }

    }

    public void analysisStock(StockAnalysisDuration duration) {
        //Get all Stocks
        try {
            for (Stock st : stockRepository.findAll()) {
                //Get stock prices during this duration
                List<Double> dailyPrices = stockDailyPriceRepository.findPriceBySymbolAndDate(st.getSymbol(), duration.getStartDate(), duration.getEndDate());
                SharpeRatio sr = new SharpeRatio(dailyPrices);
                StockAnalysis stockAnalysis = new StockAnalysis();
                stockAnalysis.setSymbol(st.getSymbol());
                stockAnalysis.setStockAnalysisDuration(duration);
                stockAnalysis.setCumulativeReturn(sr.getCumulativeReturn());
                stockAnalysis.setAverageReturn(sr.getAverageDailyReturn());
                stockAnalysis.setRiskStandardDeviation(sr.getRiskSTD());
                stockAnalysis.setSharpeRatio(sr.getSharpeRatio());
                stockAnalysisRepository.save(stockAnalysis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
