package com.liu.forever.util;

public class TimestampUtil {

    private static final ThreadLocal<Long> timestamp = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static long getCurrentTimestamp() {
        return timestamp.get();
    }
}