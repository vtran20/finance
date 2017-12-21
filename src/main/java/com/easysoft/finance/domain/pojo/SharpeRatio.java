package com.easysoft.finance.domain.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is used to calculate.
 * 1. Cumulative Return (Total return)
 * 2. Average Daily Return - mean
 * 3. Risk (Standard Deviation of Return)
 * 4. Sharpe Ratio
 */
public class SharpeRatio {
    private double cumulativeReturn = 0;
    private double averageDailyReturn = 0;
    private double riskSTD = 0;
    private double sharpeRatio = 0;

    public SharpeRatio(List<Double> stockDailyPriceList) {
        if (!(stockDailyPriceList != null && stockDailyPriceList.size() > 1)) return;

        int length = stockDailyPriceList.size();

        //Cumulative Return
        cumulativeReturn = (stockDailyPriceList.get(length-1)/stockDailyPriceList.get(0))-1;

        //Average Daily Return
        double []dailyReturn = new double[length];
        double []riskPremium = new double[length];
        double totalRiskPremium = 0;
        double riskFree = 0.001;
        double totalDailyReturn = 0;
        for (int i=0; i < length; i++) {
            if (i == 0) {
                dailyReturn[i] = 0;
            } else {
                dailyReturn[i] = (stockDailyPriceList.get(i)/stockDailyPriceList.get(i-1))-1;
                totalDailyReturn += dailyReturn[i];

                riskPremium[i] = dailyReturn[i]- riskFree;
                totalRiskPremium += riskPremium[i];
            }
        }
        averageDailyReturn = totalDailyReturn/(length-1);
        double averageRiskPremium = totalRiskPremium/(length-1);

        //Risk
        double []squares = new double[length];
        double []riskPremiumSquares = new double[length];
        double totalSquares = 0;
        double totalRiskPremiumSquares = 0;
        //don't include the first element, it always is zero
        for (int i=1; i < length; i++) {
            //For averageDailyReturn
            double difference = dailyReturn[i] - averageDailyReturn;
            squares[i] = difference * difference;
            totalSquares +=squares[i];

            //For risk premium
            double differenceRiskPremium = riskPremium[i] - averageRiskPremium;
            riskPremiumSquares[i] = differenceRiskPremium * differenceRiskPremium;
            totalRiskPremiumSquares +=riskPremiumSquares[i];
        }
        riskSTD = Math.sqrt(totalSquares/(length-2)); //n-1, don't count the first element, so that n-2
        double riskPremiumSTD = Math.sqrt(totalRiskPremiumSquares/(length-2));

        //Sharpe Ratio
        sharpeRatio = Math.sqrt(252) * (averageRiskPremium/riskPremiumSTD);
    }

    public double getCumulativeReturn() {
        return cumulativeReturn;
    }

    public void setCumulativeReturn(double cumulativeReturn) {
        this.cumulativeReturn = cumulativeReturn;
    }

    public double getAverageDailyReturn() {
        return averageDailyReturn;
    }

    public void setAverageDailyReturn(double averageDailyReturn) {
        this.averageDailyReturn = averageDailyReturn;
    }

    public double getRiskSTD() {
        return riskSTD;
    }

    public void setRiskSTD(double riskSTD) {
        this.riskSTD = riskSTD;
    }

    public double getSharpeRatio() {
        return sharpeRatio;
    }

    public void setSharpeRatio(double sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }

    public static void main(String[] args) {
        List<Double> listPrices = new ArrayList<>();
        listPrices.add(59.0);
        listPrices.add(63.0);
        listPrices.add(64.0);
        listPrices.add(62.0);
        listPrices.add(60.0);
        SharpeRatio sharpeRatio = new SharpeRatio(listPrices);
        System.out.println("Cumulative Return:"+sharpeRatio.getCumulativeReturn());
        System.out.println("Average Return:"+sharpeRatio.getAverageDailyReturn());
        System.out.println("Risk:"+sharpeRatio.getRiskSTD());
        System.out.println("Sharpe Ratio:"+sharpeRatio.getSharpeRatio());

        listPrices = new ArrayList<>();
        listPrices.add(59.0);
        listPrices.add(60.0);
        listPrices.add(61.0);
        listPrices.add(61.0);
        listPrices.add(60.0);
        sharpeRatio = new SharpeRatio(listPrices);
        System.out.println("Cumulative Return:"+sharpeRatio.getCumulativeReturn());
        System.out.println("Average Return:"+sharpeRatio.getAverageDailyReturn());
        System.out.println("Risk:"+sharpeRatio.getRiskSTD());
        System.out.println("Sharpe Ratio:"+sharpeRatio.getSharpeRatio());
    }
}
