package org.telegram.bot.services;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

/**
 * @author Dmytro Kanivets
 * @version 3.0
 */
public class BotLogger {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("Telegram Api");

    public static void trace(String tag, String msg) {
        logger.trace(MarkerFactory.getMarker(tag), msg);
    }

    public static void debug(String tag, String msg) {
        logger.debug(MarkerFactory.getMarker(tag), msg);
    }

    public static void info(String tag, String msg) {
        logger.info(MarkerFactory.getMarker(tag), msg);
    }

    public static void warn(String tag, String msg) {
        logger.warn(MarkerFactory.getMarker(tag), msg);
    }

    public static void error(String tag, String msg) {
        logger.error(MarkerFactory.getMarker(tag), msg);
    }


    public static void trace(String tag, Throwable throwable) {
        logger.trace(MarkerFactory.getMarker(tag), throwable.getMessage(), throwable);
    }

    public static void debug(String tag, Throwable throwable) {
        logger.debug(MarkerFactory.getMarker(tag), throwable.getMessage(), throwable);
    }

    public static void info(String tag, Throwable throwable) {
        logger.info(MarkerFactory.getMarker(tag), throwable.getMessage(), throwable);
    }

    public static void warn(String tag, Throwable throwable) {
        logger.warn(MarkerFactory.getMarker(tag), throwable.getMessage(), throwable);
    }

    public static void error(String tag, Throwable throwable) {
        logger.error(MarkerFactory.getMarker(tag), throwable.getMessage(), throwable);
    }


    public static void trace(String msg, String tag, Throwable throwable) {
        logger.trace(MarkerFactory.getMarker(tag), msg, throwable);
    }

    public static void debug(String msg, String tag, Throwable throwable) {
        logger.debug(MarkerFactory.getMarker(tag), msg, throwable);
    }

    public static void info(String msg, String tag, Throwable throwable) {
        logger.info(MarkerFactory.getMarker(tag), msg, throwable);
    }

    public static void warn(String msg, String tag, Throwable throwable) {
        logger.warn(MarkerFactory.getMarker(tag), msg, throwable);
    }

    public static void error(@NotNull String msg, @NotNull String tag, @NotNull Throwable throwable) {
        logger.error(MarkerFactory.getMarker(tag), msg, throwable);
    }


}
