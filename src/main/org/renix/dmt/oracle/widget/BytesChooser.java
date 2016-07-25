package org.renix.dmt.oracle.widget;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

public class BytesChooser{
    public static String sizeStr ="0B";
    Logger LOGGER = Logger.getLogger(BytesChooser.class);
    JPanel jPanel=new JPanel();
    JTextField jTextg = new JTextField("000", 3);
    JLabel jLabelg = new JLabel("G");
    JTextField jTextm = new JTextField("000", 3);
    JLabel jLabelm = new JLabel("M");
    JTextField jTextk = new JTextField("000", 3);
    JLabel jLabelk = new JLabel("K");
    JTextField jTextb = new JTextField("000", 3);
    JLabel jLabelb = new JLabel("B");
    public BytesChooser() {
        jPanel.add(jTextg);
        jPanel.add(jLabelg);
        jPanel.add(jTextm);
        jPanel.add(jLabelm);
        jPanel.add(jTextk);
        jPanel.add(jLabelk);
        jPanel.add(jTextb);
        jPanel.add(jLabelb);
        
    }
    public String getSize(){
        
        int userInput = JOptionPane.showConfirmDialog(null, jPanel,
                "请重设未分区表的最大值", 2, -1);
        sizeStr="";
        if (userInput == 0) {
            
            if(!"000".equals(jTextg.getText())){
                Long l=Long.parseLong(jTextg.getText());
                sizeStr+=l.toString()+"G";
            }
            if(!"000".equals(jTextm.getText())){
                Long l=Long.parseLong(jTextm.getText());
                sizeStr+=l.toString()+"M";
            }
            if(!"000".equals(jTextk.getText())){
                Long l=Long.parseLong(jTextk.getText());
                sizeStr+=l.toString()+"K";
            }
            if(!"000".equals(jTextb.getText())){
                Long l=Long.parseLong(jTextb.getText());
                sizeStr+=l.toString()+"B";
            }
            if(Strings.isNullOrEmpty(sizeStr)){
                sizeStr ="0B";
            }
        }
        LOGGER.info(sizeStr);
        return userInput+"-"+sizeStr;
    }
    public static void main(String[] args) {
        new BytesChooser();
    }
    
}
