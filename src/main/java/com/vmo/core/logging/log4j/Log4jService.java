package com.vmo.core.logging.log4j;

import com.vmo.core.common.CommonConstants;
import lombok.Data;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = CommonConstants.CONFIG_PREFIX + ".log4j")
@Data
public class Log4jService {

    private Boolean enable;

    private Logger logger = LogManager.getLogger(Log4jService.class);

    public Log4jService(){
        if(enable == null){
            enable = false;
        }
    }
    public void info(String message){
        if(enable){
            logger.info(message);
        }
    }

    public void warn(String message){
        if(enable){
            logger.warn(message);
        }
    }

    public void debug(String message){
        if(enable){
            logger.debug(message);
        }
    }

    public void error(String message){
        if(enable){
            logger.error(message);
        }
    }

    public void fatal(String message){
        if(enable){
            logger.fatal(message);
        }
    }

    public void log(String message){
        if(enable){
            logger.log(Level.INFO, message);
        }
    }

    public void log(Level level, String message){
        if(enable){
            logger.log(level, message);
        }
    }
}
