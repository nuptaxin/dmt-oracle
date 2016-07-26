package org.renix.dmt.oracle.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.renix.dmt.oracle.DataMigrationTool;

/**
 * 菜单栏连接并获取表结构监听器
 * 
 * @author renzx
 *
 */
public class JmiConnListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
//            expProp.setAddr(ToolApp.textExpAddr.getText());
//            expProperties.setUser(ToolApp.textExpUser.getText());
//            expProperties.setPwd(ToolApp.textExpPwd.getText());
//            expProperties.setPath(ToolApp.textExpPath.getText());
//            expProperties.setExpStartTime(ToolApp.textExpStartTime.getText());
//            expProperties.setExpEndTime(ToolApp.textExpEndTime.getText());
//            expProperties.setExpmaxSize(ToolApp.MAXSIZEINPUT.toString());
            DataMigrationTool.connDatabase();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
