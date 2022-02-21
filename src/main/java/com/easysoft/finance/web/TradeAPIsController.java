package com.easysoft.finance.web;

import com.easysoft.finance.configuration.exception.ResourceNotFoundException;
import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.finance.domain.User;
import com.easysoft.finance.domain.UserStock;
import com.easysoft.finance.domain.pojo.StockPriceDiff1DayHistory;
import com.easysoft.finance.domain.pojo.StockPriceHistory;
import com.easysoft.finance.repository.*;
import com.easysoft.finance.service.PriceService;
import com.easysoft.utils.Utils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 400 Bad Request – This means that client-side input fails validation.
 * 401 Unauthorized – This means the user isn’t not authorized to access a resource. It usually returns when the user isn’t authenticated.
 * 403 Forbidden – This means the user is authenticated, but it’s not allowed to access a resource.
 * 404 Not Found – This indicates that a resource is not found.
 * 500 Internal server error – This is a generic server error. It probably shouldn't be thrown explicitly.
 * 502 Bad Gateway – This indicates an invalid response from an upstream server.
 * 503 Service Unavailable – This indicates that something unexpected happened on server side (It can be anything like server overload, some parts of the system failed, etc.).
 * <p>
 * {
 * "status" : 400,
 * "message" : "File not found",
 * "code" : 2912,
 * "more_info" : "https://abc.com/info",
 * }
 */


@RestController
@RequestMapping("/api/v1/")
public class TradeAPIsController {
    //TODO: Implement Valid by using javax.validation.* for each body data


    private Logger log = LoggerFactory.getLogger(TradeAPIsController.class);
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
    UserRepository userRepository;
    @Autowired
    UserStockRepository userStockRepository;
    @Autowired
    PriceService priceService;

    @GetMapping("/users")
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User userRequest) {
        return userRepository.findById(userId).map(user -> {
            if (user.getFirstName() != null && !user.getFirstName().equals(userRequest.getFirstName())) {
                user.setFirstName(userRequest.getFirstName());
            }
            if (user.getLastName() != null && !user.getLastName().equals(userRequest.getLastName())) {
                user.setLastName(userRequest.getLastName());
            }
            if (user.getUsername() != null && !user.getUsername().equals(userRequest.getUsername())) {
                user.setUsername(userRequest.getUsername());
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("Not Found", "UserId " + userId + " not found", null, null));
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not Found", "UserId " + userId + " not found", null, null));
    }

    @GetMapping("/users/username/{username}")
    public User getUser(@PathVariable String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Not Found", "Username " + username + " not found", null, null));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return userRepository.findById(userId).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Not Found", "UserId " + userId + " not found", null, null));
    }

    @GetMapping("/users/{userId}/stocks")
    public List<UserStock> getAllStocksByUserId(@PathVariable(value = "userId") Long userId) {
        return userStockRepository.findByUser(userId);
    }

    @PostMapping("/users/{userId}/stock/{symbol}")
    public UserStock addUserStock(@PathVariable Long userId, @PathVariable String symbol) {
        User user = userRepository.getOne(userId);
        UserStock userStock = new UserStock();
        userStock.setUser(user);
        userStock.setSymbol(symbol);
        return userStockRepository.save(userStock);
    }

    @DeleteMapping("/users/{userId}/stock/{symbol}")
    public ResponseEntity<?> deleteUserStock(@PathVariable Long userId, @PathVariable String symbol) {
        userStockRepository.findByUserAndStock(userId, symbol).ifPresent(s -> userStockRepository.delete(s));
        return ResponseEntity.ok().build();
    }

    /**
     * Return current price of user's stocks
     *
     * @param userId
     * @param pageable
     * @return
     */
    @GetMapping("/users/{userId}/stocks/price")
    public Page<StockDailyPrice> getCurrentPriceUserStocks(@PathVariable(value = "userId") Long userId,
                                                           Pageable pageable) {
        return stockDailyPriceRepository.findByUserId(userId, Utils.getCalendarWithoutTime().getTime(), pageable);
    }

    /***************************************************************************************************/

    @GetMapping("/stocks/{symbol}/price")
    public StockDailyPrice getCurrentStockPrice(@PathVariable(value = "symbol") String symbol) {
        return stockDailyPriceRepository.findBySymbolAnDate(symbol, Utils.getCalendarWithoutTime().getTime());
    }

    @GetMapping("/stocks/{symbol}/{date}/price")
    public StockDailyPrice getCurrentStockPrice(@PathVariable(value = "symbol") String symbol, @PathVariable(value = "date") String date) {
        return stockDailyPriceRepository.findBySymbolAnDate(symbol, Utils.getCalendarWithoutTime(date).getTime());
    }

    @GetMapping("/stocks")
    public Page<Stock> getAllStocks(Pageable pageable) {
        return stockRepository.findAll(pageable);
    }

    @PostMapping("/stocks")
    public Stock createStock(@RequestBody Stock stock) {
        return stockRepository.findBySymbol(stock.getSymbol()).orElseGet(() -> {
            //save the stock
            Stock s = stockRepository.save(stock);
            //Load price history using jms
            priceService.importHistoryPrice(stock.getSymbol(), false);
            return s;
        });
    }

    @PutMapping("/stocks/{stockId}")
    public Stock updateStock(@PathVariable Long stockId, @RequestBody Stock stockRequest) {
        return stockRepository.findById(stockId).map(stock -> {
            if (stock.getName() == null || stock.getName() != null && !stock.getName().equals(stockRequest.getName())) {
                stock.setName(stockRequest.getName());
            }
            if (stock.getExchange() == null || stock.getExchange() != null && !stock.getExchange().equals(stockRequest.getExchange())) {
                stock.setExchange(stockRequest.getExchange());
            }
            return stockRepository.save(stock);
        }).orElseThrow(() -> new ResourceNotFoundException("Not Found", "Stock " + stockId + " not found", null, null));
    }

    @GetMapping("/stocks/{stockId}")
    public Stock getStock(@PathVariable Long stockId) {
        return stockRepository.findById(stockId).orElseThrow(() -> new ResourceNotFoundException("Not Found", "StockId " + stockId + " not found", null, null));
    }

    @GetMapping("/stocks/symbol/{symbol}")
    public Stock getStock(@PathVariable String symbol) {
        return stockRepository.findBySymbol(symbol).orElseThrow(() -> new ResourceNotFoundException("Not Found", "Stock " + symbol + " not found", null, null));
    }

    @DeleteMapping("/stocks/{stockId}")
    public ResponseEntity<?> deleteStock(@PathVariable Long stockId) {
        return stockRepository.findById(stockId).map(stock -> {
            stockRepository.delete(stock);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Not Found", "StockId " + stockId + " not found", null, null));
    }

    /*TODO: Implement cache to improve performance*/
    Map<String, Map<String, Object>> watchListData = new HashMap<String, Map<String, Object>>();

    @GetMapping("/users/{userId}/watchlist")
    public Map getWatchList(@PathVariable Long userId) {
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        List<StockPriceDiff1DayHistory> stockPriceHistories = new ArrayList<>();
        List<StockPriceHistory> stockPriceDiffNDays = new ArrayList<>();
        userStockRepository.findByUser(userId).forEach(userStock -> {

            Map<String, Object> watchSymbol = watchListData.get(userStock.getSymbol());
            if (watchSymbol == null) {
                watchSymbol = new HashMap<>();
                Stock stock = stockRepository.findBySymbol(userStock.getSymbol()).orElse(null);
                Calendar calendar = Utils.getCalendarWithoutTime();
                Calendar startCalendar = Utils.getCalendarWithoutTime();
                startCalendar.add(Calendar.DAY_OF_YEAR, -30);
                List<StockDailyPrice> stockDailyPrices = stockDailyPriceRepository.findBySymbolAnDate(userStock.getSymbol(), startCalendar.getTime(), calendar.getTime());

                //History price different each day
                StockPriceDiff1DayHistory stockPriceDiff1DayHistory = new StockPriceDiff1DayHistory(stockDailyPrices);
                stockPriceHistories.add(stockPriceDiff1DayHistory);

                //History price different N days
                StockPriceHistory stockPriceHistory = new StockPriceHistory(stockDailyPrices);
                stockPriceDiffNDays.add(stockPriceHistory);

                watchSymbol.put("stock", stock);
                watchSymbol.put("stockPriceHistories", stockPriceHistories);
                watchSymbol.put("stockPriceDiffNDays", stockPriceDiffNDays);

                watchListData.put(userStock.getSymbol(), watchSymbol);
            }
            result.put(userStock.getSymbol(), watchSymbol);
        });

        return result;
    }

    ////////////////////////////START ORDER///////////////////////////////////////

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

        stockPriceHistories.sort(new Comparator<StockPriceHistory>() {
            @Override
            public int compare(StockPriceHistory lhs, StockPriceHistory rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inverse for descending
                return (lhs.getDiff4days() + lhs.getDiff5days()) > (rhs.getDiff4days() + rhs.getDiff5days()) ? 1 : -1;
            }
        });

        model.addAttribute("stockPriceHistories", stockPriceHistories);
        model.addAttribute("trades", getTradeMap());
        return "diffndays";
    }

    @RequestMapping("/daily")
    public String stockDiff1DayPrice(Model model, @RequestParam(required = false, name = "sort") String sort, @RequestParam(required = false, name = "diff") Integer diff) {

        Iterator<Stock> stocks = stockRepository.findAll().iterator();
        List<StockPriceDiff1DayHistory> stockPriceHistories = new ArrayList<>();
        while (stocks.hasNext()) {
            Stock stock = stocks.next();
            Calendar calendar = Utils.getCalendarWithoutTime();
            Calendar startCalendar = Utils.getCalendarWithoutTime();
            startCalendar.add(Calendar.DAY_OF_YEAR, -50);
            List<StockDailyPrice> stockDailyPrices = stockDailyPriceRepository.findBySymbolAnDate(stock.getSymbol(), startCalendar.getTime(), calendar.getTime());
            StockPriceDiff1DayHistory stockPriceHistory = new StockPriceDiff1DayHistory(stockDailyPrices);
            if (diff != null && diff > 0) {
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
     * <p>
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
    public String reloadMonthly() {
//        taskScheduler.schedule(tasks.importMonthlyPrice(), new Date()); // schedule task for current time
        priceService.importHistoryPrice(false);
        return "redirect:/";
    }

    @RequestMapping("/reload/daily")
    public String reloadDaily() {
        //get all stocks
        List<String> ls = new ArrayList<>();
        for (Iterator<Stock> i = stockRepository.findAll().iterator(); i.hasNext(); ) {
            ls.add(i.next().getSymbol());
        }
        priceService.importStockPrice(ls);
        log.info("Import daily Price " + new Date());

        //TODO: Implement if need
        //notifyForStockCanBeSold();
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

    public Map getTradeMap() {
        Map<String, String> trades = new HashMap<>();
        for (Stock stock : stockRepository.findAll()) {
            trades.put(stock.getSymbol(), stock.getName());
        }
        return trades;
    }

    //////////////////////////////////Login/Logout///////////////////////////////////
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String login(){
//        return "login";
//    }


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
