package org.renix.dmt.oracle.util;


public class BeanUtil {

    /**
     * 更新导出参数
     * 
     * @param expProp
     * @param expAddr
     * @param expUser
     * @param expPwd
     * @param expPath
     * @param expStartTime
     * @param expEndTime
     * @param expMaxSize
     */
    public static void save2expPanel1(String addr, String userId, String file,
            String log, String bat, String maxSize, String startMonth,
            String endMonth, String startDate, String endDate,String startYear,
            String endYear) {
        Conf4Exp.ADDR = addr;
        Conf4Exp.USERID = userId;
        Conf4Exp.FILE = file;
        Conf4Exp.LOG = log;
        Conf4Exp.BAT = bat;
        Conf4Exp.MAXSIZE = maxSize;
        Conf4Exp.STARTMONTH = startMonth;
        Conf4Exp.ENDMONTH = endMonth;
        Conf4Exp.STARTDATE = startDate;
        Conf4Exp.ENDDATE = endDate;
        Conf4Exp.STARTYEAR = startYear;
        Conf4Exp.ENDYEAR = endYear;
    }
    public static void save2impPanel1(String addr, String userId,
            String log, String bat) {
        Conf4Imp.ADDR = addr;
        Conf4Imp.USERID = userId;
        Conf4Imp.LOG = log;
        Conf4Imp.BAT = bat;
        
    }

    public static void persistent2expPanel1(String addr, String userId,
            String file, String log, String bat, String maxSize,
            String startMonth, String endMonth, String startDate, String endDate, String startYear, String endYear) {
        save2expPanel1(addr, userId, file, log, bat, maxSize, startMonth, endMonth, startDate, endDate,startYear, endYear);
        Conf4Exp.getConfig().setProperty("ADDR", addr);
        Conf4Exp.getConfig().setProperty("USERID", userId);
        Conf4Exp.getConfig().setProperty("FILE", file);
        Conf4Exp.getConfig().setProperty("LOG", log);
        Conf4Exp.getConfig().setProperty("BAT", bat);
        Conf4Exp.getConfig().setProperty("MAXSIZE", maxSize);
        Conf4Exp.getConfig().setProperty("STARTMONTH", startMonth);
        Conf4Exp.getConfig().setProperty("ENDMONTH", endMonth);
        Conf4Exp.getConfig().setProperty("STARTDATE", startDate);
        Conf4Exp.getConfig().setProperty("ENDDATE", endDate);
        Conf4Exp.getConfig().setProperty("STARTYEAR", startYear);
        Conf4Exp.getConfig().setProperty("ENDYEAR", endYear);

    }
    public static void persistent2impPanel1(String addr, String userId,
            String log, String bat) {
        save2impPanel1(addr, userId, log, bat);
        Conf4Imp.getConfig().setProperty("ADDR", addr);
        Conf4Imp.getConfig().setProperty("USERID", userId);
        Conf4Imp.getConfig().setProperty("LOG", log);
        Conf4Imp.getConfig().setProperty("BAT", bat);
        
    }


}
