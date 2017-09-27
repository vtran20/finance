package com.easysoft.finance.domain.pojo;

import com.easysoft.finance.domain.Stock;
import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.utils.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Stock price history will exclude Saturday and Sunday. The price of these day should be the same with last Friday.
 */
public class StockPriceHistory {
    private List<StockDailyPrice> stockDailyPrices;
    private List<Double> priceHistory = new ArrayList<Double>();
    private String symbol;

    /*Get %diff price between the current date and diffxdays or diffxmonth*/
    private double currPrice = 0;
    private double diff1day = 0;
    private double diff2days = 0;
    private double diff3days = 0;
    private double diff4days = 0;
    private double diff5days = 0;
    private double diff10days = 0;
    private double diff15days = 0;
    private double diff20days = 0;
    private double diff30days = 0;
    private double diff40days = 0;
    private double diff50days = 0;
    private double diff60days = 0;
    private double diff70days = 0;
    private double diff80days = 0;
    private double diff90days = 0;
    private double diff100days = 0;

    public StockPriceHistory(List<StockDailyPrice> stockDailyPriceList) {
        stockDailyPrices = stockDailyPriceList;
        Calendar currCalendar = Utils.getCalendarWithoutTime();
        for (StockDailyPrice dailyPrice: stockDailyPrices) {
            symbol = dailyPrice.getSymbol();
            if (currCalendar.getTime().equals(dailyPrice.getDate())) {
                priceHistory.add(dailyPrice.getPrice());
                currCalendar.add(Calendar.DAY_OF_YEAR, -1);
            } else {
                //if weekend, move to last Friday
                while (Utils.isWeekend(currCalendar.getTime())) {
                    currCalendar.add(Calendar.DAY_OF_YEAR, -1);
                }
                // if the current date doesn't have data, set price = 0 and move to the previous day
                while (currCalendar.getTime().before(dailyPrice.getDate())) {
                    priceHistory.add(0d);
                    currCalendar.add(Calendar.DAY_OF_YEAR, -1);
                }
                // For holiday, exchange close move the currentCalendar to previous day.
                while (dailyPrice.getDate().before(currCalendar.getTime())) {
                    currCalendar.add(Calendar.DAY_OF_YEAR, -1);
                }
                // if current date match
                if (currCalendar.getTime().equals(dailyPrice.getDate())) {
                    priceHistory.add(dailyPrice.getPrice());
                    currCalendar.add(Calendar.DAY_OF_YEAR, -1);
                }
            }
        }

        if (priceHistory.size() > 0 && priceHistory.get(0) > 0) {
            if (priceHistory.size() > 1) {
                diff1day = ((priceHistory.get(0) - priceHistory.get(1))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 2) {
                diff2days = ((priceHistory.get(0) - priceHistory.get(2))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 3) {
                diff3days = ((priceHistory.get(0) - priceHistory.get(3))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 4) {
                diff4days = ((priceHistory.get(0) - priceHistory.get(4))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 5) {
                diff5days = ((priceHistory.get(0) - priceHistory.get(5))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 10) {
                diff10days = ((priceHistory.get(0) - priceHistory.get(10))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 15) {
                diff15days = ((priceHistory.get(0) - priceHistory.get(15))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 20) {
                diff20days = ((priceHistory.get(0) - priceHistory.get(20))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 30) {
                diff30days = ((priceHistory.get(0) - priceHistory.get(30))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 40) {
                diff40days = ((priceHistory.get(0) - priceHistory.get(40))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 50) {
                diff50days = ((priceHistory.get(0) - priceHistory.get(50))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 60) {
                diff60days = ((priceHistory.get(0) - priceHistory.get(60))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 70) {
                diff70days = ((priceHistory.get(0) - priceHistory.get(70))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 80) {
                diff80days = ((priceHistory.get(0) - priceHistory.get(80))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 90) {
                diff90days = ((priceHistory.get(0) - priceHistory.get(90))*100)/priceHistory.get(0);
            }
            if (priceHistory.size() > 100) {
                diff100days = ((priceHistory.get(0) - priceHistory.get(100))*100)/priceHistory.get(0);
            }
        }

    }

    public double getCurrPrice() {
        if (priceHistory.size() > 0) {
            return priceHistory.get(0);
        }
        return 0;
    }

    public double getDiff1day() {
        return diff1day;
    }

    public void setDiff1day(double diff1day) {
        this.diff1day = diff1day;
    }

    public double getDiff2days() {
        return diff2days;
    }

    public void setDiff2days(double diff2days) {
        this.diff2days = diff2days;
    }

    public double getDiff3days() {
        return diff3days;
    }

    public void setDiff3days(double diff3days) {
        this.diff3days = diff3days;
    }

    public double getDiff4days() {
        return diff4days;
    }

    public void setDiff4days(double diff4days) {
        this.diff4days = diff4days;
    }

    public double getDiff5days() {
        return diff5days;
    }

    public void setDiff5days(double diff5days) {
        this.diff5days = diff5days;
    }

    public double getDiff10days() {
        return diff10days;
    }

    public void setDiff10days(double diff10days) {
        this.diff10days = diff10days;
    }

    public double getDiff15days() {
        return diff15days;
    }

    public void setDiff15days(double diff15days) {
        this.diff15days = diff15days;
    }

    public double getDiff20days() {
        return diff20days;
    }

    public void setDiff20days(double diff20days) {
        this.diff20days = diff20days;
    }

    public double getDiff30days() {
        return diff30days;
    }

    public void setDiff30days(double diff30days) {
        this.diff30days = diff30days;
    }

    public List<StockDailyPrice> getStockDailyPrices() {
        return stockDailyPrices;
    }

    public void setStockDailyPrices(List<StockDailyPrice> stockDailyPrices) {
        this.stockDailyPrices = stockDailyPrices;
    }

    public List<Double> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<Double> priceHistory) {
        this.priceHistory = priceHistory;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setCurrPrice(double currPrice) {
        this.currPrice = currPrice;
    }

    public double getDiff40days() {
        return diff40days;
    }

    public void setDiff40days(double diff40days) {
        this.diff40days = diff40days;
    }

    public double getDiff50days() {
        return diff50days;
    }

    public void setDiff50days(double diff50days) {
        this.diff50days = diff50days;
    }

    public double getDiff60days() {
        return diff60days;
    }

    public void setDiff60days(double diff60days) {
        this.diff60days = diff60days;
    }

    public double getDiff70days() {
        return diff70days;
    }

    public void setDiff70days(double diff70days) {
        this.diff70days = diff70days;
    }

    public double getDiff80days() {
        return diff80days;
    }

    public void setDiff80days(double diff80days) {
        this.diff80days = diff80days;
    }

    public double getDiff90days() {
        return diff90days;
    }

    public void setDiff90days(double diff90days) {
        this.diff90days = diff90days;
    }

    public double getDiff100days() {
        return diff100days;
    }

    public void setDiff100days(double diff100days) {
        this.diff100days = diff100days;
    }
}
