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

    public static final Calendar CALENDAR = Calendar.getInstance();

    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT+8");

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String TIME_PATTERN = "HH:mm:ss";

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
