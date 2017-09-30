package com.easysoft.finance.web;

import com.easysoft.finance.domain.StockOrder;
import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.finance.domain.pojo.StockPriceDiff1DayHistory;
import com.easysoft.finance.domain.pojo.StockPriceHistory;
import com.easysoft.finance.repository.StockOrderRepository;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.*;

@Controller
public class TradeController {

    private Logger log = Logger.getLogger(TradeController.class);
    @Autowired
    StockRepository stockRepository;
    @Autowired
    StockOrderRepository stockOrderRepository;
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
                    } else { //New Stock
                        stock.setName(st.getName());
                        stock.setExchange(st.getStockExchange());
                        stockRepository.save(stock);
                        //Load price history
                        priceService.importHistoryPrice(150, stock.getSymbol());

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
        model.addAttribute("order", new StockOrder());
        return "/trade/order/form";
    }

    @RequestMapping(value = "/trade/order", method = RequestMethod.POST)
    public String saveOrder(StockOrder order) {
        if (order.getId() == null) {
            order.setCreatedDate(new Date());
            order.setUpdatedDate(new Date());
            stockOrderRepository.save(order);
        } else {
            StockOrder o = stockOrderRepository.findById(order.getId()).get();
            if (o != null) {
                o.setUpdatedDate(new Date());
                if (o.getSymbol() != null && !o.getSymbol().equals(order.getSymbol())) {
                    o.setSymbol(order.getSymbol());
                }
                if (o.getSent() != null && !o.getSent().equals(order.getSent())) {
                    o.setSent(order.getSent());
                }

                if (o.getBuyNum() != null && !o.getBuyNum().equals(order.getBuyNum())) {
                    o.setBuyNum(order.getBuyNum());
                }
                if (o.getBuyPrice() != order.getBuyPrice()) {
                    o.setBuyPrice(order.getBuyPrice());
                }
                if (o.getBuyFee() != order.getBuyFee()) {
                    o.setBuyFee(order.getBuyFee());
                }

                if (o.getSellNum() != null && !o.getSellNum().equals(order.getSellNum())) {
                    o.setSellNum(order.getSellNum());
                }
                if (o.getSellPrice() != order.getSellPrice()) {
                    o.setSellPrice(order.getSellPrice());
                }
                if (o.getSellFee() != order.getSellFee()) {
                    o.setSellFee(order.getSellFee());
                }
                stockOrderRepository.save(o);
            }
        }

        return "redirect:/trade/order/" + order.getId();
    }

    @RequestMapping("/trade/order/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", stockOrderRepository.findById(id).get());
        return "trade/order/view";
    }

    @RequestMapping(value = "/trade/orders", method = RequestMethod.GET)
    public String listOrder(Model model) {
        model.addAttribute("orders", stockOrderRepository.findAll());
        return "trade/order/orders";
    }

    @RequestMapping("trade/order/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", stockOrderRepository.findById(id).get());
        return "/trade/order/form";
    }

    @RequestMapping("trade/order/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        stockOrderRepository.deleteById(id);
        return "redirect:/trade/orders";
    }
    ////////////////////////////END ORDER///////////////////////////////////////

    @RequestMapping("trade/stock/reload/{symbol}")
    public String reloadPrice(@PathVariable String symbol) {
        priceService.importHistoryPrice(150, symbol);
        return "redirect:/diffndays";
    }

    @RequestMapping("trade/stock/history")
    public String stockDetailHistory(Model model, @RequestParam(required=false,name="symbol") String [] symbols) {
        if (symbols != null) {
            List<StockPriceDiff1DayHistory> stockPriceHistories = new ArrayList<>();
            List<StockPriceHistory> stockPriceDiffNDays = new ArrayList<>();
            for (String symbol : symbols) {
                Stock stock = stockRepository.findBySymbol(symbol);

                Calendar calendar = Utils.getCalendarWithoutTime();
                Calendar startCalendar = Utils.getCalendarWithoutTime();
                startCalendar.add(Calendar.DAY_OF_YEAR, -150);
                List<StockDailyPrice> stockDailyPrices = stockDailyPriceRepository.findBySymbolAnDate(symbol, startCalendar.getTime(), calendar.getTime());


                //History price different each day
                StockPriceDiff1DayHistory stockPriceDiff1DayHistory = new StockPriceDiff1DayHistory(stockDailyPrices);
                stockPriceHistories.add(stockPriceDiff1DayHistory);

                //History price different N days
                StockPriceHistory stockPriceHistory = new StockPriceHistory(stockDailyPrices);
                stockPriceDiffNDays.add(stockPriceHistory);

                model.addAttribute("stock", stock);
                model.addAttribute("stockPriceHistories", stockPriceHistories);
                model.addAttribute("stockPriceDiffNDays", stockPriceDiffNDays);

                String days = "";
                String prices = "";
                for (int i=100; i >= 0; i--) {
                    StockDailyPrice sp = stockDailyPrices.get(i);
                    if ("".equals(days)) {
                        days = String.valueOf(i);
                    } else {
                        days = days + "," + String.valueOf(i)  ;
                    }
                    if ("".equals(prices)) {
                        prices = String.valueOf(sp.getPrice());
                    } else {
                        prices = prices + "," + String.valueOf(sp.getPrice());
                    }
                }
                model.addAttribute("days", "["+days+"]");
                model.addAttribute("prices", "["+prices+"]");

            }
        }
        return "/trade/stock/history.html";
    }

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
        model.addAttribute("trades", getTradeMap());
        return "diffndays.html";
    }
    @RequestMapping("/")
    public String stockDiff1DayPrice(Model model, @RequestParam(required=false,name="sort") String sort) {

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

        if ("Y".equals(sort)) {
            Collections.sort(stockPriceHistories, new Comparator<StockPriceDiff1DayHistory>() {
                @Override
                public int compare(StockPriceDiff1DayHistory lhs, StockPriceDiff1DayHistory rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getDiff1() > rhs.getDiff1() ? 1 : -1;
                }
            });
        } else {
            Collections.sort(stockPriceHistories, new Comparator<StockPriceDiff1DayHistory>() {
                @Override
                public int compare(StockPriceDiff1DayHistory lhs, StockPriceDiff1DayHistory rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getNegativeAmount() > rhs.getNegativeAmount() ? 1 : -1;
                }
            });
        }

        model.addAttribute("stockPriceHistories", stockPriceHistories);
        model.addAttribute("trades", getTradeMap());
        return "index.html";
    }

    ///////////////////////////BACK UP/////////////////////////////////////////

    @RequestMapping(value = "/trade/stockdailyprice", method = RequestMethod.GET)
    public String listStockDailyPrice(Model model) {
        model.addAttribute("stockdailyprices", stockDailyPriceRepository.findAll());
        return "backup/stockdailyprice";
    }

    public Map getTradeMap () {
        Map<String, String> trades = new HashMap<>();
        for (Stock stock : stockRepository.findAll()) {
            trades.put(stock.getSymbol(), stock.getName());
        }
        return trades;
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
