package com.easysoft.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by vutran on 9/22/2017.
 */
public class Utils {
    public static Calendar getCalendarWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Calendar getCalendarWithoutTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Date getCurrentDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    public static Date getCurrentDateForStock() {
        return getCurrentDateForStock(null);
    }
    public static Date getCurrentDateForStock(Date date) {
        if (date == null) date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -2);
        }
        return calendar.getTime();
    }
    public static boolean isWeekend(Date date) {
        if (date == null) date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }
}
