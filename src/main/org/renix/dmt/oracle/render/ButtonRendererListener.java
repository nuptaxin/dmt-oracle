package org.renix.dmt.oracle.render;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.renix.dmt.oracle.util.CMDUtil;
import org.renix.dmt.oracle.util.Conf4Exp;

/**
 * @author renzx
 *
 */
@SuppressWarnings("serial")
public class ButtonRendererListener extends JButton implements TableCellRenderer,
        MouseInputListener {

    static Logger LOGGER = Logger.getLogger(ButtonChangeListener.class);
    // 当前监听的Table
    private JTable table = null;

    public ButtonRendererListener(JTable table) {

        setOpaque(true);
        this.table = table;

    }

    public Component getTableCellRendererComponent(JTable table, Object value,

    boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected) {

            setForeground(table.getSelectionForeground());

            setBackground(table.getSelectionBackground());

        } else {

            setForeground(table.getForeground());

            setBackground(UIManager.getColor("Button.background"));

        }

        setText((value == null) ? "" : value.toString());

        return this;

    }

    /**
     * 鼠标移出事件
     * 
     * @param e
     */
    public void mouseExited(MouseEvent e) {}

    /**
     * 鼠标拖动事件
     * 
     * @param e
     */
    public void mouseDragged(MouseEvent e) {}

    /**
     * 鼠标移动事件
     * 
     * @param e
     */
    public void mouseMoved(MouseEvent e) {}

    /**
     * 鼠标单击事件
     * 
     * @param e
     */
    public void mouseClicked(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            // 获取事件所在的行列坐标信息
            Point p = e.getPoint();
            int c = table.columnAtPoint(p);
            if (c != 3) {
                return;
            }
            int r = table.rowAtPoint(p);
            try {
                // 取得目标单元格的值,即链接信息
                String part = table.getValueAt(r, 0).toString();
                String table2 = table.getValueAt(r, 1).toString();
                String size = table.getValueAt(r, 2).toString();
                parseRow(part, table2, size);
            } catch (Exception ex) {
                Logger.getLogger(ButtonRendererListener.class.getName()).log(Level.ALL, null, ex);
            }
        }
    }

    /**
     * 鼠标按下事件
     * 
     * @param e
     */
    public void mousePressed(MouseEvent e) {}

    /**
     * 鼠标释放事件
     * 
     * @param e
     */
    public void mouseReleased(MouseEvent e) {}

    /**
     * 鼠标进入事件
     * 
     * @param e
     */
    public void mouseEntered(MouseEvent e) {}

    public static void parseRow(String part, String table, String size) {

        String path = null;

        if (part.endsWith("大容量表")) {
            path = "big_" + table;
        } else if (part.startsWith("普通表")) {
            path = "normal_" + StringUtils.substringAfter(part, "普通表");
        } else if (part.startsWith("大对象表")) {
            path = "lob_" + StringUtils.substringAfter(part, "大对象表");
        } else if (part.startsWith("分区表_月分区")) {
            path = "par_month" + StringUtils.substringAfter(part, "月分区");
        } else if (part.startsWith("分区表_年分区")) {
            path = "par_year" + StringUtils.substringAfter(part, "年分区");
        } else {
            path = "par_" + StringUtils.substringAfter(part, "分区表");
        }
        String str = null;
        if ("0B".equals(size)) {
            str =
                    "exp " + Conf4Exp.USERID + "@" + Conf4Exp.ADDR + " file=" + Conf4Exp.FILE
                            + "\\" + path + ".dmp log=" + Conf4Exp.LOG + "\\" + path
                            + ".log tables=(" + table + ") " + "buffer=" + Conf4Exp.BUFFER
                            + " compress=" + Conf4Exp.COMPRESS + " consistent="
                            + Conf4Exp.CONSISTENT + " grants=" + Conf4Exp.GRANTS + " indexes="
                            + Conf4Exp.INDEXES + " rows=N" + " triggers=" + Conf4Exp.TRIGGERS
                            + " constraints=" + Conf4Exp.CONSTRAINTS + " " + Conf4Exp.OTHER;
            LOGGER.info(str);
        } else {
            str =
                    "exp " + Conf4Exp.USERID + "@" + Conf4Exp.ADDR + " file=" + Conf4Exp.FILE
                            + "\\" + path + ".dmp log=" + Conf4Exp.LOG + "\\" + path
                            + ".log buffer=" + Conf4Exp.BUFFER + " tables=(" + table + ") "
                            + " compress=" + Conf4Exp.COMPRESS + " consistent="
                            + Conf4Exp.CONSISTENT + " grants=" + Conf4Exp.GRANTS + " indexes="
                            + Conf4Exp.INDEXES + " rows=" + Conf4Exp.ROWS + " triggers="
                            + Conf4Exp.TRIGGERS + " constraints=" + Conf4Exp.CONSTRAINTS + " "
                            + Conf4Exp.OTHER;;
            LOGGER.info(str);
        }

        File f = FileUtils.getFile(Conf4Exp.BAT + "\\" + path + ".bat");
        try {
            // 测试模式专用
            // FileUtils.writeStringToFile(f, str+"\r\n"+"pause");
            // 是否需要退出，后期置为可选项
            // FileUtils.writeStringToFile(f, str + "\r\n" + "exit");
            // 是否需要退出，后期置为可选项
            FileUtils.writeStringToFile(f, str + "\r\n");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            Runtime.getRuntime().exec(CMDUtil.getNewCmd(part, Conf4Exp.BAT + "\\" + path + ".bat"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
