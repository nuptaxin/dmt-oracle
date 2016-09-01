package org.renix.dmt.oracle.render;

import javax.swing.JOptionPane;

/**
 * @author renzx
 *
 */
public class DialogUtil {

    /**
     * TODO
     * 
     * @param message
     * @exception 展示的信息
     * @param title
     * @exception 消息框的主题
     * @param messageType
     * @exception 消息框的类型
     * @exception 只有图标不同而已类型一个有一下4种
     * @exception ERROR_MESSAGE用于错误消息
     * @exception INFORMATION_MESSAGE用于信息消息
     * @exception PLAIN_MESSAGE未使用图标
     * @exception WARNING_MESSAGE用于警告消息
     * @exception QUESTION_MESSAGE用于问题
     */
    public static void message(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    /**
     * TODO
     * 
     * @param message
     * @param title
     * @param optionType
     * @exception 指定可用于对话框的选项的整数
     * @exception 指定可用于对话框的选项的整数
     * @exception YES_NO_OPTION
     * @exception YES_NO_CANCEL_OPTION
     * @exception OK_CANCEL_OPTION
     * @param messageType
     * @exception 指定此消息种类的整数主要用于确定来自可插入外观的图标同上
     */
    public static int confirm(String message, String title, int optionType, int messageType) {
        return JOptionPane.showConfirmDialog(null, message, title, optionType, messageType);
    }

    /**
     * TODO
     * 
     * @param message
     * @param title
     * @param optionType
     * @exception 指定可用于对话框的选项的整数同上
     * @param messageType
     * @exception 指定消息种类的整数同上
     * @param options
     * @exception 指示用户可能选择的对象组成的数组
     * @exception 如果对象是组件则可以正确呈现
     * @exception 非String对象使用其 toString 方法呈现
     * @exception 如果此参数为null则由外观确定选项
     * @param initialValue
     * @exception 表示对话框的默认选择的对象
     * @exception 只有在使用options时才有意义可以为 null
     * @return
     */
    public static int option(String message, String title, int optionType, int messageType,
            String[] options, String initialValue) {
        return JOptionPane.showOptionDialog(null, message, title, optionType, messageType, null,
                options, null);
    }

    public static void main(String[] args) {
        // showMessageDialog("msg", "title", JOptionPane.PLAIN_MESSAGE);
        option("msg", "title", 2, 1, new String[] {"1", "2"}, "1");
    }
}
