package com.easysoft.finance.web;

import com.easysoft.finance.domain.*;
import com.easysoft.finance.domain.pojo.SharpeRatio;
import com.easysoft.finance.domain.pojo.StockPriceDiff1DayHistory;
import com.easysoft.finance.domain.pojo.StockPriceHistory;
import com.easysoft.finance.repository.*;
import com.easysoft.finance.service.PriceService;
import com.easysoft.utils.Utils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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
    StockAnalysisDurationRepository stockAnalysisDurationRepository;
    @Autowired
    StockAnalysisRepository stockAnalysisRepository;
    @Autowired
    PriceService priceService;

    @Autowired
    private Facebook facebook;
    @Autowired
    private ConnectionRepository connectionRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("durations", stockAnalysisDurationRepository.findAll());
        return "trade/analysis/durations";
    }

    ////////////////////////////START STOCK///////////////////////////////////////
    @RequestMapping("trade/stock/new")
    public String newStock(Model model) {
        model.addAttribute("stock", new Stock());
        return "/trade/stock/form";
    }

    @RequestMapping(value = "/trade/stock", method = RequestMethod.POST)
    public String saveStock(Stock stock) {
        if (stock != null && StringUtils.isNotEmpty(stock.getSymbol())) {
            try {
                Stock s = stockRepository.findBySymbol(stock.getSymbol());
                if (s != null) {
                    stock = s;
                    // do no thing
                } else { //New Stock
                    stockRepository.save(stock);
                    //Load price history
                    priceService.importHistoryPrice(stock.getSymbol(), true);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return "redirect:/"; //home page
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
    ////////////////////////////START ANALYSIS///////////////////////////////////////
    @RequestMapping("trade/analysis/new")
    public String newAnalysisDuration(Model model) {
        model.addAttribute("analysisDuration", new StockAnalysisDuration());
        return "/trade/analysis/form";
    }

    @RequestMapping(value = "/trade/analysisduration", method = RequestMethod.POST)
    public String saveStockAnalysisDuration(StockAnalysisDuration stockAnalysisDuration) {
        if (stockAnalysisDuration != null && stockAnalysisDuration.getStartDate() != null && stockAnalysisDuration.getEndDate() != null) {
            try {
                StockAnalysisDuration s = stockAnalysisDurationRepository.findByDuration(stockAnalysisDuration.getStartDate(), stockAnalysisDuration.getEndDate());
                if (s != null) {
                    stockAnalysisDuration = s;
                    // do no thing
                } else { //New Stock
                    stockAnalysisDuration = stockAnalysisDurationRepository.save(stockAnalysisDuration);
                    //Analysis stock in this duration
                    priceService.analysisStock(stockAnalysisDuration);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return "redirect:/"; //home page
        }
        return "redirect:/trade/analysis/" + stockAnalysisDuration.getId();
    }

    @RequestMapping("/trade/analysis/rerun/{id}")
    public String viewStockAnalysis(@PathVariable Long id, Model model) {
        //Get all Stocks
        try {
            for (Stock st : stockRepository.findAll()) {
                //Get stock prices during this duration
                List<StockAnalysis> stockAnalysises = stockAnalysisRepository.findByDurationBySymbol(id, st.getSymbol());
                if (stockAnalysises != null && !stockAnalysises.isEmpty()) {
                    //Do nothing
                } else {
                    StockAnalysisDuration stockAnalysisDuration = stockAnalysisDurationRepository.findById(id).get();
                    if (stockAnalysisDuration != null) {
                        List<Double> dailyPrices = stockDailyPriceRepository.findPriceBySymbolAndDate(st.getSymbol(), stockAnalysisDuration.getStartDate(), stockAnalysisDuration.getEndDate());
                        SharpeRatio sr = new SharpeRatio(dailyPrices);
                        StockAnalysis stockAnalysis = new StockAnalysis();
                        stockAnalysis.setSymbol(st.getSymbol());
                        stockAnalysis.setStockAnalysisDuration(stockAnalysisDuration);
                        stockAnalysis.setCumulativeReturn(sr.getCumulativeReturn());
                        stockAnalysis.setAverageReturn(sr.getAverageDailyReturn());
                        stockAnalysis.setRiskStandardDeviation(sr.getRiskSTD());
                        stockAnalysis.setSharpeRatio(sr.getSharpeRatio());
                        stockAnalysisRepository.save(stockAnalysis);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("durations", stockAnalysisDurationRepository.findAll());
        return "trade/analysis/durations";
    }
    @RequestMapping("/trade/analysis/{id}")
    public String viewStockAnalysis(@PathVariable Long id, Model model,@RequestParam(required=false,name="sort") String sort, @RequestParam(required=false,name="column") String column) {
        List<StockAnalysis> stockAnalysises = stockAnalysisRepository.findByDuration(id);
        if ("Y".equals(sort)) {
            if ("sharpeRatio".equals(column)) {
                Collections.sort(stockAnalysises, new Comparator<StockAnalysis>() {
                    @Override
                    public int compare(StockAnalysis lhs, StockAnalysis rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        return (lhs.getSharpeRatio()) > (rhs.getSharpeRatio()) ? -1 : 1;
                    }
                });
            } else if ("risk".equals(column)) {
                Collections.sort(stockAnalysises, new Comparator<StockAnalysis>() {
                    @Override
                    public int compare(StockAnalysis lhs, StockAnalysis rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        return (lhs.getRiskStandardDeviation()) > (rhs.getRiskStandardDeviation()) ? -1 : 1;
                    }
                });

            } else if ("average".equals(column)) {
                Collections.sort(stockAnalysises, new Comparator<StockAnalysis>() {
                    @Override
                    public int compare(StockAnalysis lhs, StockAnalysis rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        return (lhs.getAverageReturn()) > (rhs.getAverageReturn()) ? -1 : 1;
                    }
                });

            } else if ("cumulative".equals(column)) {
                Collections.sort(stockAnalysises, new Comparator<StockAnalysis>() {
                    @Override
                    public int compare(StockAnalysis lhs, StockAnalysis rhs) {
                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                        return (lhs.getCumulativeReturn()) > (rhs.getCumulativeReturn()) ? -1 : 1;
                    }
                });
            }
        } else {
            Collections.sort(stockAnalysises, new Comparator<StockAnalysis>() {
                @Override
                public int compare(StockAnalysis lhs, StockAnalysis rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return (lhs.getSharpeRatio()) > (rhs.getSharpeRatio()) ? -1 : 1;
                }
            });
        }

        model.addAttribute("stockAnalysis", stockAnalysises);
        model.addAttribute("stockAnalysisDuration", stockAnalysisDurationRepository.findById(id).get());
        return "trade/analysis/analysis";
    }

    @RequestMapping(value = "/trade/durations", method = RequestMethod.GET)
    public String listAnalysisDuration(Model model) {
        model.addAttribute("durations", stockAnalysisDurationRepository.findAll());
        return "trade/analysis/durations";
    }
    ////////////////////////////END ANALYSIS///////////////////////////////////////

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
    /*
    * Reload stock should be last 100 days
    * */
    @RequestMapping("trade/stock/reload/{symbol}")
    public String reloadPrice(@PathVariable String symbol) {
        priceService.importHistoryPrice(symbol, false);
        return "redirect:/diffndays";
    }

    @RequestMapping("trade/stock/history")
    public String stockDetailHistory(Model model, @RequestParam(required=false,name="symbol") String [] symbols, @RequestParam(required=false,name="duration") Integer duration) {
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

                StringBuilder days = new StringBuilder();
                StringBuilder prices = new StringBuilder();
                if (duration == null || duration<= 0) {
                    for (int i = stockDailyPrices.size() -1 ; i >= 0; i--) {
                        StockDailyPrice sp = stockDailyPrices.get(i);
                        if (days.length() <=0) {
                            days.append(String.valueOf(i));
                        } else {
                            days.append(",");
                            days.append(String.valueOf(i));
                        }
                        if (prices.length() <= 0) {
                            prices.append(String.valueOf(sp.getPrice()));
                        } else {
                            prices.append(",");
                            prices.append(String.valueOf(sp.getPrice()));
                        }
                    }
                } else {
                    startCalendar = Utils.getCalendarWithoutTime();
                    Date ed = startCalendar.getTime();
                    startCalendar.add(Calendar.DAY_OF_YEAR, -duration);
                    Date sd = startCalendar.getTime();
                    log.info(sd);
                    log.info(ed);
                    List<Double> priceList = stockDailyPriceRepository.findPriceBySymbolAndDate(symbol, sd, ed);
                    log.info(priceList.size());
                    for(int i = 0; i < priceList.size(); i++){
                        Double price = priceList.get(i);
                        if (prices.length() <= 0) {
                            prices.append(price);
                            days.append(priceList.size() - i) ;
                        } else {
                            prices.append(",");
                            prices.append(price);
                            days.append(",");
                            days.append(priceList.size() - i);
                        }

                    }

                }
                model.addAttribute("days", "["+days+"]");
                model.addAttribute("prices", "["+prices+"]");
            }
        }
        return "/trade/stock/history";
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
            //Fix issue in the case stockDailyPrices is empty
            if (StringUtils.isEmpty(stockPriceHistory.getSymbol())) {
                stockPriceHistory.setSymbol(stock.getSymbol());
            }
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
        return "diffndays";
    }
    @RequestMapping("/daily")
    public String stockDiff1DayPrice(Model model, @RequestParam(required=false,name="sort") String sort, @RequestParam(required=false,name="diff") Integer diff) {

                Iterator<Stock> stocks = stockRepository.findAll().iterator();
        List<StockPriceDiff1DayHistory> stockPriceHistories = new ArrayList<>();
        while (stocks.hasNext()) {
            Stock stock = stocks.next();
            Calendar calendar = Utils.getCalendarWithoutTime();
            Calendar startCalendar = Utils.getCalendarWithoutTime();
            startCalendar.add(Calendar.DAY_OF_YEAR, -50);
            List<StockDailyPrice> stockDailyPrices = stockDailyPriceRepository.findBySymbolAnDate(stock.getSymbol(), startCalendar.getTime(), calendar.getTime());
            StockPriceDiff1DayHistory stockPriceHistory = new StockPriceDiff1DayHistory(stockDailyPrices);
            if (diff != null && diff>0) {
                stockPriceHistory.setTotalPercentDiff(diff);
            }
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
        return "daily";
    }

    /**
     * Suggest buying stock based on these features:
     * - Decrease price today (x2): x
     * - Decrease price last 5 recent days: y
     * - Decrease price last 10 recent days: z
     *
     * Rating = 2x + y + z
     *
     * @return
     */
    @RequestMapping("/suggestion")
    public String stockSuggestion(Model model) {
        Iterator<Stock> stocks = stockRepository.findAll().iterator();
        List<StockPriceDiff1DayHistory> stockPriceHistories = new ArrayList<>();
        while (stocks.hasNext()) {
            Stock stock = stocks.next();
            Calendar calendar = Utils.getCalendarWithoutTime();
            Calendar startCalendar = Utils.getCalendarWithoutTime();
            startCalendar.add(Calendar.DAY_OF_YEAR, -50);
            List<StockDailyPrice> stockDailyPrices = stockDailyPriceRepository.findBySymbolAnDate(stock.getSymbol(), startCalendar.getTime(), calendar.getTime());
            StockPriceDiff1DayHistory stockPriceHistory = new StockPriceDiff1DayHistory(stockDailyPrices);
            //Fix issue in the case stockDailyPrices is empty
            if (StringUtils.isEmpty(stockPriceHistory.getSymbol())) {
                stockPriceHistory.setSymbol(stock.getSymbol());
            }
            stockPriceHistories.add(stockPriceHistory);
        }
        Collections.sort(stockPriceHistories, new Comparator<StockPriceDiff1DayHistory>() {
            @Override
            public int compare(StockPriceDiff1DayHistory lhs, StockPriceDiff1DayHistory rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getTotalDecrease() > rhs.getTotalDecrease() ? 1 : -1;
            }
        });

        model.addAttribute("stockPriceHistories", stockPriceHistories);
        model.addAttribute("trades", getTradeMap());
        return "daily";

    }
    @RequestMapping("/reload")
    public String reloadDaily() {
        priceService.importHistoryPrice(false);
        return "redirect:/";
    }
    @RequestMapping("/reloadhistory")
    public String reloadHistory() {
        priceService.importHistoryPrice(true);
        return "redirect:/";
    }


        ///////////////////////////BACK UP/////////////////////////////////////////

    @RequestMapping(value = "/trade/stockdailyprice", method = RequestMethod.GET)
    public String listStockDailyPrice(Model model) {
        model.addAttribute("stockdailyprices", stockDailyPriceRepository.findAll());
        return "connect/stockdailyprice";
    }

    public Map getTradeMap () {
        Map<String, String> trades = new HashMap<>();
        for (Stock stock : stockRepository.findAll()) {
            trades.put(stock.getSymbol(), stock.getName());
        }
        return trades;
    }

    //////////////////////////////////Login/Logout///////////////////////////////////
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }


    ///////////////////////Facebook Connect//////////////////////////////////////////
    @RequestMapping(value = "/facebook", method = RequestMethod.GET)
    public String helloFacebook(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebookConnect";
        }

        model.addAttribute("facebookProfile", facebook.userOperations().getUserProfile());
        PagedList<Post> feed = facebook.feedOperations().getFeed();
        model.addAttribute("feed", feed);
        return "hello";
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

    @RequestMapping(value = "test.json", method = RequestMethod.GET)
    public
    @ResponseBody
    Map test() throws Exception {
        Calendar calendar = Utils.getCalendarWithoutTime();
        Calendar startCalendar = Utils.getCalendarWithoutTime();
        startCalendar.add(Calendar.DAY_OF_YEAR, -150);
        List<Double> stockDailyPrices = stockDailyPriceRepository.findPriceBySymbolAndDate("TSLA", startCalendar.getTime(), calendar.getTime());
        Map result = new HashMap();
        result.put("draw", 1);
        result.put("recordsTotal", stockDailyPrices.size());
        result.put("recordsFiltered", stockDailyPrices.size());
        result.put("data", stockDailyPrices);
        return result;
    }

}
