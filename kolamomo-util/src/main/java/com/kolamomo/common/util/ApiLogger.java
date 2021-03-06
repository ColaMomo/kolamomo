package com.kolamomo.common.util;

import org.apache.log4j.Logger;


public class ApiLogger {
	private static Logger debugLog = Logger.getLogger("debug");
	private static Logger infoLog = Logger.getLogger("info");
	private static Logger warnLog = Logger.getLogger("warn");
	private static Logger errorLog = Logger.getLogger("error");
    
    public static void debug(String message) {
    	debugLog.info(message);
    }

    public static void info(String message) {
    	infoLog.info(message);
    }
    
    public static void warn(String message) {
    	warnLog.info(message);
    }

    public static void error(String message) {
    	errorLog.info(message);
    }

}
