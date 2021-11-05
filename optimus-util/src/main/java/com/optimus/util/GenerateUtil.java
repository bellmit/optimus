package com.optimus.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GenerateUtil
 */
public class GenerateUtil {

    private static final AtomicInteger POINT = new AtomicInteger(0);

    private static final Integer MIN = 1;

    private static final Integer MAX = 999999;

    private static final String INFIX_COVER = "%015d";

    private static final String SUFFIX_COVER = "%06d";

    /**
     * generate
     * 
     * @param prefix
     * @return
     */
    public static String generate(String prefix) {

        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(infix());
        sb.append(suffix());

        return sb.toString();

    }

    /**
     * infix
     * 
     * @return
     */
    private static String infix() {

        Long value = System.currentTimeMillis();

        return String.format(INFIX_COVER, value);

    }

    /**
     * suffix
     * 
     * @return
     */
    private static String suffix() {

        ThreadLocalRandom tlr = ThreadLocalRandom.current();

        Integer l = tlr.nextInt(MIN, MAX);
        Integer r = POINT.incrementAndGet();

        if (r.compareTo(MAX) > 0) {

            POINT.set(tlr.nextInt(MIN, MAX));
            r = POINT.get();

        }

        return String.format(SUFFIX_COVER, l) + String.format(SUFFIX_COVER, r);

    }

}
