package com.kpi.bot.database.lucene;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexableField;

import java.time.Instant;
import java.util.Date;

public interface IndexableFieldFactory {
    IndexableField getField(String name, Object o);

    static IndexableFieldFactory getIndexableFieldFactory(Class<?> type) {
        if (CharSequence.class.isAssignableFrom(type)) {
            return (name, o) -> new StringField(name, o.toString(), Field.Store.NO);
        } else if (Double.class.isAssignableFrom(type)) {
            return (name, o) -> new DoublePoint(name, ((Double) o).doubleValue());
        } else if (Float.class.isAssignableFrom(type)) {
            return (name, o) -> new FloatPoint(name, ((Float) o).floatValue());
        } else if (Long.class.isAssignableFrom(type)) {
            return (name, o) -> new LongPoint(name, ((Long) o).longValue());
        } else if (Integer.class.isAssignableFrom(type)) {
            return (name, o) -> new IntPoint(name, ((Integer) o).intValue());
        } else if (Date.class.isAssignableFrom(type)) {
            return (name, o) -> new StringField(name, DateTools.timeToString(((Date) o).toInstant().toEpochMilli(), DateTools.Resolution.MILLISECOND), Field.Store.NO);
        } else if (Instant.class.isAssignableFrom(type)) {
            return (name, o) -> new StringField(name, DateTools.timeToString(((Instant) o).toEpochMilli(), DateTools.Resolution.MILLISECOND), Field.Store.NO);
        } else {
            throw new RuntimeException("Can not find appropriate type for " + type.getName());
        }
    }
}