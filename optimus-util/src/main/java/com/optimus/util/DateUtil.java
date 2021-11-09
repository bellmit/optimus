package com.optimus.util;

import java.util.Date;

/**
 * DateUtil
 * 
 * @author sunxp
 */
public class DateUtil {

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * currentDate
     * 
     * @return
     */
    public static Date currentDate() {

        return new Date();

    }

}
