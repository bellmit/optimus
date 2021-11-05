package com.optimus.util;

import java.util.Date;

/**
 * Dateutil
 */
public class Dateutil {

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * 当前日期
     * 
     * @return
     */
    public static Date currentDate() {

        return new Date();

    }

}
