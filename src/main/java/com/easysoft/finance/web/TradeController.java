package com.easysoft.finance.web;

import com.easysoft.finance.domain.Order;
import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.finance.domain.pojo.StockPriceDiff1DayHistory;
import com.easysoft.finance.domain.pojo.StockPriceHistory;
import com.easysoft.finance.repository.OrderRepository;
import com.easysoft.finance.repository.StockDailyPriceRepository;
import com.easysoft.finance.repository.StockRepository;
import com.easysoft.finance.service.PriceService;
import com.easysoft.utils.Utils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.*;

@Controller
public class TradeController {

    private Logger log = Logger.getLogger(TradeController.class);
    @Autowired
    StockRepository stockRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    StockDailyPriceRepository stockDailyPriceRepository;
    @Autowired
    PriceService priceService;

    ////////////////////////////START STOCK///////////////////////////////////////
    @RequestMapping("trade/stock/new")
    public String newStock(Model model) {
        model.addAttribute("stock", new Stock());
        return "/trade/stock/form";
    }

    @RequestMapping(value = "/trade/stock", method = RequestMethod.POST)
    public String saveStock(Stock stock) {
        if (StringUtils.isNotEmpty(stock.getSymbol())) {
            try {
                yahoofinance.Stock st = YahooFinance.get(stock.getSymbol());
                if (st != null && StringUtils.isNotEmpty(st.getCurrency())) {
                    Stock s = stockRepository.findBySymbol(stock.getSymbol());
                    if (s != null) {
                        stock = s;
                    } else {
                        stock.setName(st.getName());
                        stock.setExchange(st.getStockExchange());
                        stockRepository.save(stock);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/trade/stock/" + stock.getId();
    }

    @RequestMapping("/trade/stock/{id}")
    public String viewStock(@PathVariable Long id, Model model) {
        model.addAttribute("stock", stockRepository.findById(id).get());
        return "trade/stock/view";
    }

    @RequestMapping(value = "/trade/stocks", method = RequestMethod.GET)
    public String listStock(Model model) {
        model.addAttribute("stocks", stockRepository.findAll());
        return "trade/stock/stocks";
    }

    @RequestMapping("trade/stock/edit/{id}")
    public String editStock(@PathVariable Long id, Model model) {
        model.addAttribute("stock", stockRepository.findById(id).get());
        return "/trade/stock/form";
    }

    @RequestMapping("trade/stock/delete/{id}")
    public String deleteStock(@PathVariable Long id) {
        stockRepository.deleteById(id);
        return "redirect:/trade/stocks";
    }
    ////////////////////////////END STOCK///////////////////////////////////////
    ////////////////////////////START ORDER///////////////////////////////////////
    @RequestMapping("trade/order/new")
    public String newOrder(Model model) {
        model.addAttribute("order", new Order());
        return "/trade/order/form";
    }

    @RequestMapping(value = "/trade/order", method = RequestMethod.POST)
    public String saveOrder(Order order) {
        orderRepository.save(order);
        return "redirect:/trade/order/" + order.getId();
    }

    @RequestMapping("/trade/order/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).get());
        return "trade/order/view";
    }

    @RequestMapping(value = "/trade/orders", method = RequestMethod.GET)
    public String listOrder(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "trade/order/orders";
    }

    @RequestMapping("trade/order/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).get());
        return "/trade/order/form";
    }

    @RequestMapping("trade/order/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return "redirect:/trade/orders";
    }
    ////////////////////////////END ORDER///////////////////////////////////////

    @RequestMapping("/diffndays")
    public String stockPrice(Model model) {
                Iterator<Stock> stocks = stockRepository.findAll().iterator();
        List<StockPriceHistory> stockPriceHistories = new ArrayList<>();
        while (stocks.hasNext()) {
            Stock stock = stocks.next();
            Calendar calendar = Utils.getCalendarWithoutTime();
            Calendar startCalendar = Utils.getCalendarWithoutTime();
            startCalendar.add(Calendar.DAY_OF_YEAR, -150);
            List<StockDailyPrice> stockDailyPrices = stockDailyPriceRepository.findBySymbolAnDate(stock.getSymbol(), startCalendar.getTime(), calendar.getTime());
            StockPriceHistory stockPriceHistory = new StockPriceHistory(stockDailyPrices);
            stockPriceHistories.add(stockPriceHistory);
        }

        Collections.sort(stockPriceHistories, new Comparator<StockPriceHistory>() {
            @Override
            public int compare(StockPriceHistory lhs, StockPriceHistory rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return (lhs.getDiff4days() + lhs.getDiff5days()) > (rhs.getDiff4days() + rhs.getDiff5days()) ? 1 : -1;
            }
        });

        model.addAttribute("stockPriceHistories", stockPriceHistories);
        return "diffndays.html";
    }
    @RequestMapping("/")
    public String stockDiff1DayPrice(Model model) {
                Iterator<Stock> stocks = stockRepository.findAll().iterator();
        List<StockPriceDiff1DayHistory> stockPriceHistories = new ArrayList<>();
        while (stocks.hasNext()) {
            Stock stock = stocks.next();
            Calendar calendar = Utils.getCalendarWithoutTime();
            Calendar startCalendar = Utils.getCalendarWithoutTime();
            startCalendar.add(Calendar.DAY_OF_YEAR, -50);
            List<StockDailyPrice> stockDailyPrices = stockDailyPriceRepository.findBySymbolAnDate(stock.getSymbol(), startCalendar.getTime(), calendar.getTime());
            StockPriceDiff1DayHistory stockPriceHistory = new StockPriceDiff1DayHistory(stockDailyPrices);
            stockPriceHistories.add(stockPriceHistory);
        }

        Collections.sort(stockPriceHistories, new Comparator<StockPriceDiff1DayHistory>() {
            @Override
            public int compare(StockPriceDiff1DayHistory lhs, StockPriceDiff1DayHistory rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getNegativeAmount() > rhs.getNegativeAmount() ? 1 : -1;
            }
        });


        model.addAttribute("stockPriceHistories", stockPriceHistories);
        return "index.html";
    }

    ///////////////////////////BACK UP/////////////////////////////////////////

    @RequestMapping(value = "/trade/stockdailyprice", method = RequestMethod.GET)
    public String listStockDailyPrice(Model model) {
        model.addAttribute("stockdailyprices", stockDailyPriceRepository.findAll());
        return "backup/stockdailyprice";
    }


    @RequestMapping(value = "stocks.json", method = RequestMethod.GET)
    public
    @ResponseBody
    Map findAllStocks() throws Exception {
//        Stock stock = new Stock();
//        stock.setName("Apple");
//        stock.setSymbol("AAPL");
//        stockRepository.save(stock);
//
//        log.info("Saved Stock - id: " + stock.getId());
//
//        stock = new Stock();
//        stock.setName("TESLA");
//        stock.setSymbol("TSLA");
//        stockRepository.save(stock);
        List<Stock> stocks = IteratorUtils.toList(stockRepository.findAll().iterator());
        Map result = new HashMap();
        result.put("draw", 1);
        result.put("recordsTotal", stocks.size());
        result.put("recordsFiltered", stocks.size());
        result.put("data", stocks);
        return result;
    }

}
