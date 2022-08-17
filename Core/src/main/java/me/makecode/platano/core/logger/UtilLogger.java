package me.makecode.platano.core.logger;

import org.slf4j.Logger;

public class UtilLogger {

    private java.util.logging.Logger javaLogger;
    private org.slf4j.Logger slf4jLogger;

    public UtilLogger(Object logger){
        if(java.util.logging.Logger.class.isAssignableFrom(logger.getClass())){
            this.javaLogger = (java.util.logging.Logger) logger;
        } else {
            this.slf4jLogger = (Logger) logger;
        }
        info("UtilLogger loaded");
    }

    public void info(String message){
        if(javaLogger != null){
            javaLogger.info(message);
            return;
        }
        slf4jLogger.info(message);
    }

    public void error(String message){
        if(javaLogger != null){
            javaLogger.severe(message);
            return;
        }
        slf4jLogger.error(message);
    }

}
