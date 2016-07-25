package org.renix.dmt.oracle.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class DateUtil {
    public static boolean checkDate(String dateStr) {
        boolean isDate = true;
        try {
            //使用严格的日期验证模式
            Date date = DateUtils.parseDateStrictly(dateStr,
                    new String[] { "yyyy-MM-dd" });
            // 使用宽松的日期验证模式
            // Date date=DateUtils.parseDate(dateStr, new
            // String[]{"yyyyMMdd-HHmmss"});
            System.out.println(date);
        } catch (ParseException e) {
            isDate = false;
            e.printStackTrace();
        }
        return isDate;
    }
}
