package com.tilem.flashcards.util;

import org.slf4j.*;

/**
 * Wrapper methods and utilities for log4j loggers
 * @author kk
 *
 */
public class LogWrapper {
	
	private Logger realLogger;
	
	private LogWrapper(Logger logger) {
		realLogger = logger;
	}
	
	/** 
	 * Return a LogWrapper containing the logger for class clazz
	 * <p>example: final static LogWrapper logger = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());
	 * @param clazz
	 * @return
	 */
	public static LogWrapper getLogger(Class<?> clazz) {
		return LogWrapper.getLogger(clazz.getName());
	}

	/**
	 * Return a LogWrapper containing a logger with name loggerName
	 * @param loggerName
	 * @return
	 */
	public static LogWrapper getLogger(String loggerName) {
		return new LogWrapper(LoggerFactory.getLogger(loggerName));
	}

	/**
	 * Wrapper methods
	 */
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