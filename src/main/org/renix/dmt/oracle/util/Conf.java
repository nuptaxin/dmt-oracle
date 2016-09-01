package org.renix.dmt.oracle.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * @author renzx
 *
 */
public class Conf {
    static Logger LOGGER = Logger.getLogger(Conf.class);
    static public Integer TABLESNUM = null;
    private static PropertiesConfiguration config = null;
    static {
        initConf();
    }

    public static void initConf() {
        config = new PropertiesConfiguration();
        config.setEncoding("UTF-8");
        config.setFileName("conf/conf.properties");
        config.setAutoSave(true);
        try {
            config.load();
        } catch (ConfigurationException e) {
            LOGGER.error("参数文件读取失败");
            e.printStackTrace();
        }
        TABLESNUM = config.getInt("TABLESNUM", 31);
    }

    public static final PropertiesConfiguration getConfig() {
        return config;
    }

    public static final void setConfig(PropertiesConfiguration config) {
        Conf.config = config;
    }

}
