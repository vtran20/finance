package com.easysoft.finance.domain.pojo;

import com.easysoft.finance.domain.StockDailyPrice;
import com.easysoft.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Stock price history will exclude Saturday and Sunday. The price of these day should be the same with last Friday.
 */
public class StockPriceDiff1DayHistory {
    private List<StockDailyPrice> stockDailyPrices;
    private List<Double> priceHistory = new ArrayList<Double>();
    private String symbol;
    private int totalPercentDiff = 5;
    /**
     * Suggest buying stock based on these features:
     * - Decrease price today (x2): x
     * - Decrease price last 5 recent days: y
     * - Decrease price last 10 recent days: z
     *
     * Rating = 2x + y + z
    */
     private double totalDecrease = 0;

    /*Get %diff price between the current date and previous day*/
    private double currPrice = 0;
    private double diff1 = 0;
    private double diff2 = 0;
    private double diff3 = 0;
    private double diff4 = 0;
    private double diff5 = 0;
    private double diff6 = 0;
    private double diff7 = 0;
    private double diff8 = 0;
    private double diff9 = 0;
    private double diff10 = 0;
    private double diff11 = 0;
    private double diff12 = 0;
    private double diff13 = 0;
    private double diff14 = 0;
    private double diff15 = 0;
    private double diff16 = 0;
    private double diff17 = 0;
    private double diff18 = 0;
    private double diff19 = 0;
    private double diff20 = 0;
    private double diff21 = 0;
    private double diff22 = 0;
    private double diff23 = 0;
    private double diff24 = 0;
    private double diff25 = 0;
    private double diff26 = 0;
    private double diff27 = 0;
    private double diff28 = 0;
    private double diff29 = 0;
    private double diff30 = 0;

    private List<Double> sortList = new ArrayList<>();
    public StockPriceDiff1DayHistory(List<StockDailyPrice> stockDailyPriceList) {
        stockDailyPrices = stockDailyPriceList;
        Calendar currCalendar = Utils.getCalendarWithoutTime();
        for (StockDailyPrice dailyPrice: stockDailyPrices) {
            symbol = dailyPrice.getSymbol();
            priceHistory.add(dailyPrice.getPrice());
        }

        if (priceHistory.size() > 1 && priceHistory.get(1) > 0) {
            diff1 = ((priceHistory.get(0)*100)/priceHistory.get(1)) - 100;
        }
        if (priceHistory.size() > 2 && priceHistory.get(2) > 0) {
            diff2 = ((priceHistory.get(1)*100)/priceHistory.get(2)) - 100;
        }
        if (priceHistory.size() > 3 && priceHistory.get(3) > 0) {
            diff3 = ((priceHistory.get(2)*100)/priceHistory.get(3)) - 100;
        }
        if (priceHistory.size() > 4 && priceHistory.get(4) > 0) {
            diff4 = ((priceHistory.get(3)*100)/priceHistory.get(4)) - 100;
        }
        if (priceHistory.size() > 5 && priceHistory.get(5) > 0) {
            diff5 = ((priceHistory.get(4)*100)/priceHistory.get(5)) - 100;
        }
        if (priceHistory.size() > 6 && priceHistory.get(6) > 0) {
            diff6 = ((priceHistory.get(5)*100)/priceHistory.get(6)) - 100;
        }
        if (priceHistory.size() > 7 && priceHistory.get(7) > 0) {
            diff7 = ((priceHistory.get(6)*100)/priceHistory.get(7)) - 100;
        }
        if (priceHistory.size() > 8 && priceHistory.get(8) > 0) {
            diff8 = ((priceHistory.get(7)*100)/priceHistory.get(8)) - 100;
        }
        if (priceHistory.size() > 9 && priceHistory.get(9) > 0) {
            diff9 = ((priceHistory.get(8)*100)/priceHistory.get(9)) - 100;
        }
        if (priceHistory.size() > 10 && priceHistory.get(10) > 0) {
            diff10 = ((priceHistory.get(9)*100)/priceHistory.get(10)) - 100;
        }
        if (priceHistory.size() > 11 && priceHistory.get(11) > 0) {
            diff11 = ((priceHistory.get(10)*100)/priceHistory.get(11)) - 100;
        }
        if (priceHistory.size() > 12 && priceHistory.get(12) > 0) {
            diff12 = ((priceHistory.get(11)*100)/priceHistory.get(12)) - 100;
        }
        if (priceHistory.size() > 13 && priceHistory.get(13) > 0) {
            diff13 = ((priceHistory.get(12)*100)/priceHistory.get(13)) - 100;
        }
        if (priceHistory.size() > 14 && priceHistory.get(14) > 0) {
            diff14 = ((priceHistory.get(13)*100)/priceHistory.get(14)) - 100;
        }
        if (priceHistory.size() > 15 && priceHistory.get(15) > 0) {
            diff15 = ((priceHistory.get(14)*100)/priceHistory.get(15)) - 100;
        }
        if (priceHistory.size() > 16 && priceHistory.get(16) > 0) {
            diff16 = ((priceHistory.get(15)*100)/priceHistory.get(16)) - 100;
        }
        if (priceHistory.size() > 17 && priceHistory.get(17) > 0) {
            diff17 = ((priceHistory.get(16)*100)/priceHistory.get(17)) - 100;
        }
        if (priceHistory.size() > 18 && priceHistory.get(18) > 0) {
            diff18 = ((priceHistory.get(17)*100)/priceHistory.get(18)) - 100;
        }
        if (priceHistory.size() > 19 && priceHistory.get(19) > 0) {
            diff19 = ((priceHistory.get(18)*100)/priceHistory.get(19)) - 100;
        }
        if (priceHistory.size() > 20 && priceHistory.get(20) > 0) {
            diff20 = ((priceHistory.get(19)*100)/priceHistory.get(20)) - 100;
        }
        if (priceHistory.size() > 21 && priceHistory.get(21) > 0) {
            diff21 = ((priceHistory.get(20)*100)/priceHistory.get(21)) - 100;
        }
        if (priceHistory.size() > 22 && priceHistory.get(22) > 0) {
            diff22 = ((priceHistory.get(21)*100)/priceHistory.get(22)) - 100;
        }
        if (priceHistory.size() > 23 && priceHistory.get(23) > 0) {
            diff23 = ((priceHistory.get(22)*100)/priceHistory.get(23)) - 100;
        }
        if (priceHistory.size() > 24 && priceHistory.get(24) > 0) {
            diff24 = ((priceHistory.get(23)*100)/priceHistory.get(24)) - 100;
        }
        if (priceHistory.size() > 25 && priceHistory.get(25) > 0) {
            diff25 = ((priceHistory.get(24)*100)/priceHistory.get(25)) - 100;
        }
        if (priceHistory.size() > 26 && priceHistory.get(26) > 0) {
            diff26 = ((priceHistory.get(25)*100)/priceHistory.get(26)) - 100;
        }
        if (priceHistory.size() > 27 && priceHistory.get(27) > 0) {
            diff27 = ((priceHistory.get(26)*100)/priceHistory.get(27)) - 100;
        }
        if (priceHistory.size() > 28 && priceHistory.get(28) > 0) {
            diff28 = ((priceHistory.get(27)*100)/priceHistory.get(28)) - 100;
        }
        if (priceHistory.size() > 29 && priceHistory.get(29) > 0) {
            diff29 = ((priceHistory.get(28)*100)/priceHistory.get(29)) - 100;
        }
        if (priceHistory.size() > 30 && priceHistory.get(30) > 0) {
            diff30 = ((priceHistory.get(29)*100)/priceHistory.get(30)) - 100;
        }

        sortList.add(diff1);
        sortList.add(diff2);
        sortList.add(diff3);
        sortList.add(diff4);
        sortList.add(diff5);
        sortList.add(diff6);
        sortList.add(diff7);
        sortList.add(diff8);
        sortList.add(diff9);
        sortList.add(diff10);
        sortList.add(diff11);
        sortList.add(diff12);
        sortList.add(diff13);
        sortList.add(diff14);
        sortList.add(diff15);
        sortList.add(diff16);
        sortList.add(diff17);
        sortList.add(diff18);
        sortList.add(diff19);
        sortList.add(diff20);
        sortList.add(diff21);
        sortList.add(diff22);
        sortList.add(diff23);
        sortList.add(diff24);
        sortList.add(diff25);
        sortList.add(diff26);
        sortList.add(diff27);
        sortList.add(diff28);
        sortList.add(diff29);
        sortList.add(diff30);

        /*Total decrease: Value = 2x + y + z*/
        totalDecrease = 2*diff1 + getDecreaseAmount(5) + getDecreaseAmount(10);
    }

    /**
     * - Total diff the last 5 days.
     * - If the sixth one and so on is positive then count on them as well.
     *
     * @return
     */
    public double getNegativeAmount() {
        double negativeAmount = 0;
        if (sortList.size() > totalPercentDiff) {
            for (int i=0; i < totalPercentDiff; i++) {
                negativeAmount += sortList.get(i);
            }

            for (int i=totalPercentDiff; i < sortList.size(); i++) {
                double value = sortList.get(i);
                if (value <= 0) {
                    negativeAmount += value;
                } else {
                    break;
                }
            }
        }
        return negativeAmount;
    }
    public double getDecreaseAmount(int days) {
        double negativeAmount = 0;
        if (sortList.size() > days) {
            for (int i=0; i < days; i++) {
                negativeAmount += sortList.get(i);
            }

            for (int i=days; i < sortList.size(); i++) {
                double value = sortList.get(i);
                if (value <= 0) {
                    negativeAmount += value;
                } else {
                    break;
                }
            }
        }
        return negativeAmount;
    }

    public double getCurrPrice() {
        if (priceHistory.size() > 0) {
            return priceHistory.get(0);
        }
        return 0;
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

    public double getDiff1() {
        return diff1;
    }

    public void setDiff1(double diff1) {
        this.diff1 = diff1;
    }

    public double getDiff2() {
        return diff2;
    }

    public void setDiff2(double diff2) {
        this.diff2 = diff2;
    }

    public double getDiff3() {
        return diff3;
    }

    public void setDiff3(double diff3) {
        this.diff3 = diff3;
    }

    public double getDiff4() {
        return diff4;
    }

    public void setDiff4(double diff4) {
        this.diff4 = diff4;
    }

    public double getDiff5() {
        return diff5;
    }

    public void setDiff5(double diff5) {
        this.diff5 = diff5;
    }

    public double getDiff6() {
        return diff6;
    }

    public void setDiff6(double diff6) {
        this.diff6 = diff6;
    }

    public double getDiff7() {
        return diff7;
    }

    public void setDiff7(double diff7) {
        this.diff7 = diff7;
    }

    public double getDiff8() {
        return diff8;
    }

    public void setDiff8(double diff8) {
        this.diff8 = diff8;
    }

    public double getDiff9() {
        return diff9;
    }

    public void setDiff9(double diff9) {
        this.diff9 = diff9;
    }

    public double getDiff10() {
        return diff10;
    }

    public void setDiff10(double diff10) {
        this.diff10 = diff10;
    }

    public double getDiff11() {
        return diff11;
    }

    public void setDiff11(double diff11) {
        this.diff11 = diff11;
    }

    public double getDiff12() {
        return diff12;
    }

    public void setDiff12(double diff12) {
        this.diff12 = diff12;
    }

    public double getDiff13() {
        return diff13;
    }

    public void setDiff13(double diff13) {
        this.diff13 = diff13;
    }

    public double getDiff14() {
        return diff14;
    }

    public void setDiff14(double diff14) {
        this.diff14 = diff14;
    }

    public double getDiff15() {
        return diff15;
    }

    public void setDiff15(double diff15) {
        this.diff15 = diff15;
    }

    public double getDiff16() {
        return diff16;
    }

    public void setDiff16(double diff16) {
        this.diff16 = diff16;
    }

    public double getDiff17() {
        return diff17;
    }

    public void setDiff17(double diff17) {
        this.diff17 = diff17;
    }

    public double getDiff18() {
        return diff18;
    }

    public void setDiff18(double diff18) {
        this.diff18 = diff18;
    }

    public double getDiff19() {
        return diff19;
    }

    public void setDiff19(double diff19) {
        this.diff19 = diff19;
    }

    public double getDiff20() {
        return diff20;
    }

    public void setDiff20(double diff20) {
        this.diff20 = diff20;
    }

    public double getDiff21() {
        return diff21;
    }

    public void setDiff21(double diff21) {
        this.diff21 = diff21;
    }

    public double getDiff22() {
        return diff22;
    }

    public void setDiff22(double diff22) {
        this.diff22 = diff22;
    }

    public double getDiff23() {
        return diff23;
    }

    public void setDiff23(double diff23) {
        this.diff23 = diff23;
    }

    public double getDiff24() {
        return diff24;
    }

    public void setDiff24(double diff24) {
        this.diff24 = diff24;
    }

    public double getDiff25() {
        return diff25;
    }

    public void setDiff25(double diff25) {
        this.diff25 = diff25;
    }

    public double getDiff26() {
        return diff26;
    }

    public void setDiff26(double diff26) {
        this.diff26 = diff26;
    }

    public double getDiff27() {
        return diff27;
    }

    public void setDiff27(double diff27) {
        this.diff27 = diff27;
    }

    public double getDiff28() {
        return diff28;
    }

    public void setDiff28(double diff28) {
        this.diff28 = diff28;
    }

    public double getDiff29() {
        return diff29;
    }

    public void setDiff29(double diff29) {
        this.diff29 = diff29;
    }

    public double getDiff30() {
        return diff30;
    }

    public void setDiff30(double diff30) {
        this.diff30 = diff30;
    }

    public int getTotalPercentDiff() {
        return totalPercentDiff;
    }

    public void setTotalPercentDiff(int totalPercentDiff) {
        this.totalPercentDiff = totalPercentDiff;
    }

    public double getTotalDecrease() {
        return totalDecrease;
    }

    public void setTotalDecrease(double totalDecrease) {
        this.totalDecrease = totalDecrease;
    }
}
