package com.softcloud.simplereadstatus.newsStatusUtils;

import java.util.Calendar;

/**
 * Created by j-renzhexin on 2016/6/30.
 */
public class TimeUtils {

    public static long getTodayStartTimeMillis() {
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
        today.set( Calendar.HOUR_OF_DAY, 0);
        today.set( Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        return today.getTimeInMillis();
    }

    public static boolean isToday(long timeMillis) {
        long todayStartTime = getTodayStartTimeMillis();
        return timeMillis >= todayStartTime && timeMillis < todayStartTime + UnitUtils.DAY;
    }

    public static boolean isYesterday(long timeMillis) {
        long todayStartTime = getTodayStartTimeMillis();
        return timeMillis >= todayStartTime - UnitUtils.DAY && timeMillis < todayStartTime;
    }

    public static boolean isTomorrow(long timeMillis) {
        long todayStartTime = getTodayStartTimeMillis();
        return timeMillis >= todayStartTime + UnitUtils.DAY && timeMillis < todayStartTime + UnitUtils.DAY * 2;
    }
}
