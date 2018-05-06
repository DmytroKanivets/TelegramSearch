package com.kpi.bot.database.lucene;

import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexableField;

import java.time.Instant;
import java.util.Date;

public interface IndexableFieldFactory {
    IndexableField getField(String name, Object o);

    static IndexableFieldFactory getIndexableFieldFactory(Class<?> type) {
        if (CharSequence.class.isAssignableFrom(type)) {
            return (name, o) -> new StringField(name, o.toString(), Field.Store.NO);
        } else if (Double.class.isAssignableFrom(type)) {
            return (name, o) -> new DoublePoint(name, FormatConverter.toDouble(o));
        } else if (Float.class.isAssignableFrom(type)) {
            return (name, o) -> new DoublePoint(name, FormatConverter.toDouble(o));
        } else if (Long.class.isAssignableFrom(type)) {
            return (name, o) -> new LongPoint(name, FormatConverter.toLong(o));
        } else if (Integer.class.isAssignableFrom(type)) {
            return (name, o) -> new LongPoint(name, FormatConverter.toLong(o));
        } else if (Date.class.isAssignableFrom(type)) {
            return (name, o) -> new LongPoint(name, FormatConverter.toLong(o));
        } else if (Instant.class.isAssignableFrom(type)) {
            return (name, o) -> new LongPoint(name, FormatConverter.toLong(o));
        } else {
            throw new RuntimeException("Can not find appropriate type for " + type.getName());
        }
    }
}