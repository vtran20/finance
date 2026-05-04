package com.tdameritrade.stock;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vutran on 11/2/2017.
 */
public class MetaInfo extends Base {
    @JsonProperty("2. Symbol")
    private String symbol;
    @JsonProperty("3. Last Refreshed")
    private String lastRefreshed;
    @JsonProperty("4. Interval")
    private String interval;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getLastRefreshed() {
        if (lastRefreshed != null) {
            try {
               return new SimpleDateFormat(DATE_FORMAT).parse(lastRefreshed);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public void setLastRefreshed(String lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
