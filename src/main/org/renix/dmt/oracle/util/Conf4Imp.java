package org.renix.dmt.oracle.util;

import javax.annotation.PostConstruct;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Conf4Imp {
    static Logger LOGGER = Logger.getLogger(Conf4Imp.class);
    static public String ADDR = null;
    static public String USERID = null;
    static public String BAT = null;
    static public String LOG = null;
    
    static public String BUFFER = null;
    static public String FULL = null;
    static public String GRANTS = null;
    static public String INDEXES = null;
    static public String ROWS = null;
    static public String CONSTRAINTS = null;
    static public String IGNORE = null;
    static public String OTHER = null;
    private static PropertiesConfiguration config = null;

    public static final PropertiesConfiguration getConfig() {
        return config;
    }

    public static final void setConfig(PropertiesConfiguration config) {
        Conf4Imp.config = config;
    }

    @PostConstruct
    public static void initConf() {
        config = new PropertiesConfiguration();
        config.setEncoding("UTF-8");
        config.setFileName("conf/imp.properties");
        config.setAutoSave(true);
        try {
            config.load();
        } catch (ConfigurationException e) {
            LOGGER.error("参数文件读取失败");
            e.printStackTrace();
        }
        ADDR = config.getString("ADDR", "127.0.0.1/orcl");
        USERID = config.getString("USERID", "CJDYFX/orcl");
        BAT = config.getString("BATPATH", "c:\\test\\bat");
        LOG = config.getString("LOGPATH", "c:\\test\\log");

        BUFFER = config.getString("BUFFER", "819200000");
        FULL = config.getString("FULL", "Y");
        GRANTS = config.getString("GRANTS", "N");
        INDEXES = config.getString("INDEXES", "Y");
        ROWS = config.getString("ROWS", "Y");
        CONSTRAINTS = config.getString("CONSTRAINTS", "Y");
        IGNORE = config.getString("IGNORE", "Y");
        OTHER = config.getString("OTHER", "");
    }
    
    public static void reReadImp1() {
        ADDR = config.getString("ADDR", "127.0.0.1/orcl");
        USERID = config.getString("USERID", "CJDYFX/orcl");
        BAT = config.getString("BAT", "c:\\test\\bat");
        LOG = config.getString("LOG", "c:\\test\\log");
    }
    public static void reReadImp2() {
        BUFFER = config.getString("BUFFER", "819200000");
        FULL = config.getString("FULL", "Y");
        GRANTS = config.getString("GRANTS", "N");
        INDEXES = config.getString("INDEXES", "Y");
        ROWS = config.getString("ROWS", "Y");
        CONSTRAINTS = config.getString("CONSTRAINTS", "Y");
        IGNORE = config.getString("IGNORE", "Y");
        OTHER = config.getString("OTHER", "");
    }
}
