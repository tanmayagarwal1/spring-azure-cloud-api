package com.spring.azure.springazurecloud.utils;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@UtilityClass
public class LogHelper {
    private static final Logger logger;
    static {
        logger = LogManager.getLogger();
    }

    public static void logInfo(Object message){
        logger.info(message);
    }

    public static void logError(String message, Throwable err){
        logger.error(message, err);
    }

    public static void logDebug(String message, Throwable err){
        logger.debug(message, err);
    }

}
