package com.tilem.flashcards.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogWrapper {

	private final Logger realLogger;

	private LogWrapper(Logger logger) {
		realLogger = logger;
	}

	public static LogWrapper getLogger(Class<?> clazz) {
		return LogWrapper.getLogger(clazz.getName());
	}

	public static LogWrapper getLogger(String loggerName) {
		return new LogWrapper(LoggerFactory.getLogger(loggerName));
	}

	public void error(String message) {
		realLogger.error(message);
	}

	public void error(String message, Exception e) {
		realLogger.error(message, e);
	}

	public void warning(String message, Exception e) {
		realLogger.warn(message, e);
	}

	public void warning(String message) {
		warning(message, null);
	}

	public void info(String message) {
		realLogger.info(message);
	}

	public void debug(String message) {
		realLogger.debug(message);
	}
}