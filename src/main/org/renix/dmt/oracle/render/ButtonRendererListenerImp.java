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
import org.apache.log4j.Logger;
import org.renix.dmt.oracle.util.CMDUtil;
import org.renix.dmt.oracle.util.Conf4Imp;

@SuppressWarnings("serial")
public class ButtonRendererListenerImp extends JButton implements
        TableCellRenderer, MouseInputListener {

    static Logger LOGGER = Logger.getLogger(ButtonChangeListener.class);
    // 当前监听的Table
    private JTable table = null;

    public ButtonRendererListenerImp(JTable table) {

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
    public void mouseExited(MouseEvent e) {
    }

    /**
     * 鼠标拖动事件
     * 
     * @param e
     */
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * 鼠标移动事件
     * 
     * @param e
     */
    public void mouseMoved(MouseEvent e) {
    }

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
            if (c != 2) {
                return;
            }
            int r = table.rowAtPoint(p);
            try {
                // 取得目标单元格的值,即链接信息
                String fileName = table.getValueAt(r, 0).toString();
                String filepath = table.getValueAt(r, 1).toString();
                LOGGER.info(fileName + filepath);
                parseRow(filepath, fileName);
            } catch (Exception ex) {
                LOGGER.warn(ex);
            }
        }
    }

    /**
     * 鼠标按下事件
     * 
     * @param e
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * 鼠标释放事件
     * 
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * 鼠标进入事件
     * 
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
    }

    public static void parseRow(String filePath, String fileName) {

        String fileNameImp = fileName.substring(0, fileName.length() - 4)
                .concat("Imp");
        String str = "imp " + Conf4Imp.USERID + "@" + Conf4Imp.ADDR + " file="
                + filePath + " log=" + Conf4Imp.LOG + "\\" + fileNameImp
                + ".log buffer=819200000 full=" + Conf4Imp.FULL + " ignore="
                + Conf4Imp.IGNORE + " grants=" + Conf4Imp.GRANTS + " indexes="
                + Conf4Imp.INDEXES + " rows=" + Conf4Imp.ROWS + " constraints="
                + Conf4Imp.CONSTRAINTS + " "+Conf4Imp.OTHER;
        LOGGER.info(str);
        File f = FileUtils.getFile(Conf4Imp.BAT + "\\" + fileNameImp + ".bat");
        try {
            // 测试模式专用
            // FileUtils.writeStringToFile(f, str + "\r\n" + "pause");
            // 非测试环境
            // FileUtils.writeStringToFile(f, str + "\r\n" + "exit");
            // 非测试环境，结束后不退出
            FileUtils.writeStringToFile(f, str + "\r\n");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            Runtime.getRuntime().exec(
                    CMDUtil.getNewCmd(fileNameImp, Conf4Imp.BAT + "\\"
                            + fileNameImp + ".bat"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
