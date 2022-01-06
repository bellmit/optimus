package com.optimus.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * DateUtil
 * 
 * @author sunxp
 */
public class DateUtil {

    /** 日历实例 */
    private static final Calendar CALENDAR = Calendar.getInstance();

    static {

        // 默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

    }

    /**
     * currentDate
     * 
     * @return
     */
    public static Date currentDate() {

        CALENDAR.setTimeInMillis(System.currentTimeMillis());
        return CALENDAR.getTime();

    }

    /**
     * offsetForMinute
     * 
     * @param date
     * @param offset
     * @return
     */
    public static Date offsetForMinute(Date date, int offset) {

        CALENDAR.setTime(date);
        CALENDAR.add(Calendar.MINUTE, offset);

        return CALENDAR.getTime();

    }

}
