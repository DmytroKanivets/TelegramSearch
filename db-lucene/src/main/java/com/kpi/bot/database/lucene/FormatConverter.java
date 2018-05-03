package com.kpi.bot.database.lucene;

import java.time.Instant;
import java.util.Date;

public interface FormatConverter {
    static Long toLong(Object o) {
        if (Date.class.isAssignableFrom(o.getClass())) {
            return ((Date) o).toInstant().toEpochMilli();
        } else if (Instant.class.isAssignableFrom(o.getClass())) {
            return ((Instant) o).toEpochMilli();
        } else if (Long.class.isAssignableFrom(o.getClass())) {
            return (Long) o;
        } else if (Integer.class.isAssignableFrom(o.getClass())) {
            return ((Integer) o).longValue();
        } else {
            System.out.println(o.getClass());
            return Long.parseLong(o.toString());
        }
    }

    static Double toDouble(Object o) {
        if (Double.class.isAssignableFrom(o.getClass())) {
            return (Double) o;
        } else if (Float.class.isAssignableFrom(o.getClass())) {
            return ((Float) o).doubleValue();
        } else {
            return Double.parseDouble(o.toString());
        }
    }
}
