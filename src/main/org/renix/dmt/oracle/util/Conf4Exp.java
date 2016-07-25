package org.renix.dmt.oracle.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class Conf4Exp {
    static Logger LOGGER = Logger.getLogger(Conf4Exp.class);
    static public String ADDR = null;
    static public String USERID = null;
    static public String BAT = null;
    static public String FILE = null;
    static public String LOG = null;
    static public String STARTMONTH = null;
    static public String ENDMONTH = null;
    static public String STARTDATE = null;
    static public String ENDDATE = null;
    static public String STARTYEAR = null;
    static public String ENDYEAR = null;
    static public String MAXSIZE = null;
    static public Long MAXBYTE = null;
    static public String BUFFER = null;
    static public String COMPRESS = null;
    static public String CONSISTENT = null;
    static public String GRANTS = null;
    static public String INDEXES = null;
    static public String ROWS = null;
    static public String TRIGGERS = null;
    static public String CONSTRAINTS = null;
    static public String OTHER = null;
    private static PropertiesConfiguration config = null;
    static{
        initConf();
    }

    public static void initConf() {
        config = new PropertiesConfiguration();
        config.setEncoding("UTF-8");
        config.setFileName("conf/exp.properties");
        config.setAutoSave(true);
        try {
            config.load();
        } catch (ConfigurationException e) {
            LOGGER.error("参数文件读取失败");
            e.printStackTrace();
        }
        ADDR = config.getString("ADDR", "127.0.0.1/orcl");
        USERID = config.getString("USERID", "CJDYFX/orcl");
        BAT = config.getString("BAT", "c:\\test\\bat");
        FILE = config.getString("FILE", "c:\\test");
        LOG = config.getString("LOG", "c:\\test\\log");
        STARTMONTH = config.getString("STARTMONTH", "2015-11");
        ENDMONTH = config.getString("ENDMONTH", "2015-12");
        STARTDATE = config.getString("STARTDATE", "2015-11-01");
        ENDDATE = config.getString("ENDDATE", "2015-12-01");
        STARTYEAR = config.getString("STARTYEAR", "2015");
        ENDYEAR = config.getString("ENDYEAR", "2016");
        MAXSIZE = config.getString("MAXSIZE", "100M");
        MAXBYTE=TransferSizeUtil.Str2Long(MAXSIZE);
        
        BUFFER = config.getString("BUFFER", "819200000");
        COMPRESS = config.getString("COMPRESS", "N");
        CONSISTENT = config.getString("CONSISTENT", "N");
        GRANTS = config.getString("GRANTS", "N");
        INDEXES = config.getString("INDEXES", "Y");
        ROWS = config.getString("ROWS", "Y");
        TRIGGERS = config.getString("TRIGGERS", "Y");
        CONSTRAINTS = config.getString("CONSTRAINTS", "Y");
        OTHER = config.getString("OTHER", "");
    }

    public static final PropertiesConfiguration getConfig() {
        return config;
    }

    public static final void setConfig(PropertiesConfiguration config) {
        Conf4Exp.config = config;
    }

    public static void reReadExp1() {
        ADDR = config.getString("ADDR", "127.0.0.1/orcl");
        USERID = config.getString("USERID", "CJDYFX/orcl");
        BAT = config.getString("BAT", "c:\\test\\bat");
        FILE = config.getString("FILE", "c:\\test");
        LOG = config.getString("LOG", "c:\\test\\log");
        STARTMONTH = config.getString("STARTMONTH", "2015-11");
        ENDMONTH = config.getString("ENDMONTH", "2015-12");
        STARTDATE = config.getString("STARTTIME", "2015-11-01");
        ENDDATE = config.getString("ENDTIME", "2015-12-01");
        STARTYEAR = config.getString("STARTYEAR", "2015");
        ENDYEAR = config.getString("ENDYEAR", "2016");
        MAXSIZE = config.getString("MAXSIZE", "100M");
    }
    public static void reReadExp2() {
        BUFFER = config.getString("BUFFER", "819200000");
        COMPRESS = config.getString("COMPRESS", "N");
        CONSISTENT = config.getString("CONSISTENT", "N");
        GRANTS = config.getString("GRANTS", "N");
        INDEXES = config.getString("INDEXES", "Y");
        ROWS = config.getString("ROWS", "Y");
        TRIGGERS = config.getString("TRIGGERS", "Y");
        CONSTRAINTS = config.getString("CONSTRAINTS", "Y");
        OTHER = config.getString("OTHER", "");
    }
}
