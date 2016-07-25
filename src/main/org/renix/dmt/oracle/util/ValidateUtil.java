package org.renix.dmt.oracle.util;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.renix.dmt.oracle.errorenum.ParamError;
import org.renix.dmt.oracle.render.DirectoryUtil;

import com.google.common.base.Strings;

public class ValidateUtil {
    public static ParamError validateParam(int mode) {
        // 数据库连接参数验证
        String[] Urlstr = StringUtils.split(Conf4Exp.ADDR, "/");
        if (Urlstr.length != 2)
            return ParamError.PARAM_ERROR_URL;

        if (Strings.isNullOrEmpty(Conf4Exp.USERID))
            return ParamError.PARAM_ERROR_USER;

        // 导出参数单独验证
        if (mode == 1) {
            // 文件夹验证
            if (!DirectoryUtil.checkDirectory(Conf4Exp.FILE)
                    || !DirectoryUtil.checkDirectory(Conf4Exp.LOG)
                    || !DirectoryUtil.checkDirectory(Conf4Exp.BAT)) {
                int result = JOptionPane.showConfirmDialog(null, "是否创建文件夹"
                        + Conf4Exp.FILE + "以及其子文件夹logs、data", "文件夹创建",
                        JOptionPane.OK_CANCEL_OPTION);
                if (result == 0) {
                    DirectoryUtil.makeDirectory(Conf4Exp.FILE);
                    DirectoryUtil.makeDirectory(Conf4Exp.LOG);
                    DirectoryUtil.makeDirectory(Conf4Exp.BAT);
                } else
                    return ParamError.PARAM_ERROR_EXPPATH;
            }
            // 日期验证
            if (!DateUtil.checkDate(Conf4Exp.STARTDATE))
                return ParamError.PARAM_ERROR_STARTTIME;
            if (!DateUtil.checkDate(Conf4Exp.ENDDATE))
                return ParamError.PARAM_ERROR_ENDTIME;
        }

        // 重新配置数据库连接池和sql2o
        DatabaseUtil.reConn(Conf4Exp.ADDR, Conf4Exp.USERID);

        // 测试数据库是否能够连通，如果不能连通则给出提示
        if (!DatabaseUtil.connTest())
            return ParamError.PARAM_ERROR_CONN;

        return ParamError.PARAM_PASS;
    }

    public static ParamError validateImpParam(int mode) {
        // 数据库连接参数验证
        String[] Urlstr = StringUtils.split(Conf4Imp.ADDR, "/");
        if (Urlstr.length != 2)
            return ParamError.PARAM_ERROR_URL;

        if (Strings.isNullOrEmpty(Conf4Imp.USERID))
            return ParamError.PARAM_ERROR_USER;

        // 导出参数单独验证
        if (mode == 1) {
            // 文件夹验证
            if (!DirectoryUtil.checkDirectory(Conf4Imp.LOG)
                    || !DirectoryUtil.checkDirectory(Conf4Imp.BAT)) {
                int result = JOptionPane.showConfirmDialog(null, "是否创建文件夹",
                        "文件夹创建", JOptionPane.OK_CANCEL_OPTION);
                if (result == 0) {

                    DirectoryUtil.makeDirectory(Conf4Imp.LOG);
                    DirectoryUtil.makeDirectory(Conf4Imp.BAT);
                } else
                    return ParamError.PARAM_ERROR_EXPPATH;
            }

        }

        // 重新配置数据库连接池和sql2o
        DatabaseUtil.reConn(Conf4Imp.ADDR, Conf4Imp.USERID);

        // 测试数据库是否能够连通，如果不能连通则给出提示
        if (!DatabaseUtil.connTest())
            return ParamError.PARAM_ERROR_CONN;

        return ParamError.PARAM_PASS;
    }
}
