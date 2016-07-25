package org.renix.dmt.oracle.util;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

public class TransferSizeUtil {

    public static String Long2Str(Long bytes) {
        String bytes1 = null;
        List<Long> ls1 = Lists.newArrayList();
        DecimalFormat df = new DecimalFormat("####.00");
        while (bytes > 0) {
            ls1.add(bytes % 1000);
            bytes = bytes / 1000;
        }
        switch (ls1.size()) {
        case 0:
            bytes1 = "0B";
            break;
        case 1:
            bytes1 = ls1.get(0) + "B";
            break;
        case 2:
            Double dk = ls1.get(1) + ls1.get(0) / 1000.0;
            bytes1 = df.format(dk) + "K";
            break;
        case 3:
            Double dm = ls1.get(2) + ls1.get(1) / 1000.0;
            bytes1 = df.format(dm) + "M";
            break;
        case 4:
            Double dg = ls1.get(3) + ls1.get(2) / 1000.0;
            bytes1 = df.format(dg) + "G";
            break;

        default:
            Double dx = 0d;
            for (int i = ls1.size() - 1; i >= 3; i++) {
                dx += ls1.get(i);
            }
            dx += ls1.get(2) / 1000.0;
            bytes1 = df.format(dx) + "G";
            break;
        }
        return bytes1;
    }

    public static Long Str2Long(String strByte) {
        Long byteLong = 0l;
        if (StringUtils.contains(strByte, "G")) {
            String[] s1 = StringUtils.split(strByte, "G");
            byteLong += Long.parseLong(s1[0]) * 1000000000l;
            if (s1.length > 1)
                strByte = s1[1];
            else
                return byteLong;
        }
        if (StringUtils.contains(strByte, "M")) {
            String[] s1 = StringUtils.split(strByte, "M");
            byteLong += Long.parseLong(s1[0]) * 1000000l;
            if (s1.length > 1)
                strByte = s1[1];
            else
                return byteLong;
        }
        if (StringUtils.contains(strByte, "K")) {
            String[] s1 = StringUtils.split(strByte, "K");
            byteLong += Long.parseLong(s1[0]) * 1000l;
            if (s1.length > 1)
                strByte = s1[1];
            else
                return byteLong;
        }
        if (StringUtils.contains(strByte, "B")) {
            String s1 = StringUtils.substringBefore(strByte, "B");
            byteLong += Long.parseLong(s1);
        }
        return byteLong;
    }

}
