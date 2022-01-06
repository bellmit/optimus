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

    private static final Calendar CALENDAR = Calendar.getInstance();

    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT+8");

    static {

        TimeZone.setDefault(DEFAULT_TIME_ZONE);

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
