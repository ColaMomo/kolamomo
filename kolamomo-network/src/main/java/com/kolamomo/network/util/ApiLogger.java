package com.kolamomo.network.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ApiLogger {
	private static Logger debugLog = Logger.getLogger("debug");
	private static Logger infoLog = Logger.getLogger("info");
	private static Logger warnLog = Logger.getLogger("warn");
	private static Logger errorLog = Logger.getLogger("error");

    public ApiLogger() {
        PropertyConfigurator.configure("log4j.properties");
    }
    
    public static void debug(String message) {

        System.out.println(message);
        debugLog.info(message);
    }

    public static void info(String message) {
    	System.out.println(message);
        infoLog.info(message);
    }
    
    public static void warn(String message) {

        System.out.println(message);
        warnLog.info(message);
    }

    public static void error(String message) {

        System.out.println(message);
        errorLog.info(message);
    }
}
