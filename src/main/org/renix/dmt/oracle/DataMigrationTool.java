package org.renix.dmt.oracle;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.renix.dmt.oracle.bean.DataValue;
import org.renix.dmt.oracle.bean.TmpExpData;
import org.renix.dmt.oracle.errorenum.ParamError;
import org.renix.dmt.oracle.render.ButtonRendererListener;
import org.renix.dmt.oracle.render.ButtonRendererListenerImp;
import org.renix.dmt.oracle.render.CellRenderer;
import org.renix.dmt.oracle.render.CellRendererImp;
import org.renix.dmt.oracle.render.CellToolTip;
import org.renix.dmt.oracle.render.DialogUtil;
import org.renix.dmt.oracle.render.DisplayJFrame;
import org.renix.dmt.oracle.util.BeanUtil;
import org.renix.dmt.oracle.util.Conf;
import org.renix.dmt.oracle.util.Conf4Exp;
import org.renix.dmt.oracle.util.Conf4Imp;
import org.renix.dmt.oracle.util.DatabaseUtil;
import org.renix.dmt.oracle.util.TransferSizeUtil;
import org.renix.dmt.oracle.util.ValidateUtil;
import org.renix.dmt.oracle.widget.BytesChooser;
import org.renix.dmt.oracle.widget.DateChooser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings("serial")
public class DataMigrationTool extends JFrame {
    Logger LOGGER = Logger.getLogger(DataMigrationTool.class);
    public final static String expToolName = "数据库导入导出工具";
    public static ApplicationContext ctx = null;
    public static String[] tableHeadStr = { "类型", "表名", "大小", "操作" };
    public static String[] tableHeadImpStr = { "文件名", "文件路径", "操作" };
    Field[] f = Conf4Exp.class.getDeclaredFields();
    Field[] fImp = Conf4Imp.class.getDeclaredFields();

    // 菜单类
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuExp = new JMenu("导出");
    private JMenu menuImp = new JMenu("导入");
    private JMenu menuHelp = new JMenu("帮助");
    private JMenuItem menuItemConn = new JMenuItem("连接并获取表结构");
    private JMenuItem menuItemConnTest = new JMenuItem("数据库连接测试");
    private JMenuItem menuItemAddFile = new JMenuItem("添加文件");
    private JMenuItem menuItemAddDir = new JMenuItem("添加文件夹");
    private JMenuItem menuItemAboutUs = new JMenuItem("关于软件");
    private JMenuItem menuItemContact = new JMenuItem("联系我们");
    // 选项卡
    JTabbedPane tabPane = new JTabbedPane();
    JPanel tabExp = new JPanel();
    JPanel tabImp = new JPanel();

    // 选项卡1
    // 面板分隔类
    JPanel tabExpParamPanel = new JPanel();
    JPanel tabExpParamPanelS1U = new JPanel(new GridLayout(6, 4));
    JPanel tabExpParamPanelS1D = new JPanel();
    JPanel tabExpParamPanelS2U = new JPanel(new GridLayout(3, 4));
    JPanel tabExpParamPanelS2D = new JPanel();
    JPanel tabExpParamPanelS3U = new JPanel(new GridLayout(6, 4));
    JPanel tabExpParamPanelS3D = new JPanel();
    JPanel tabExpMainPanel = new JPanel(new GridLayout(1, 1));
    JPanel tabExpStatusPanel = new JPanel(new GridLayout(1, 3));

    // 选项卡上部分
    // 参数类JLable和JTextField
    JLabel labExpAddr = new JLabel("数据库地址：");
    JLabel labExpUserId = new JLabel("用户名/密码：");
    JLabel labExpFile = new JLabel("数据文件：");
    JLabel labExpLog = new JLabel("日志文件：");
    JLabel labExpBat = new JLabel("Bat文件：");
    JLabel labExpMaxSize = new JLabel("大文件筛选：");
    JLabel labExpStartMonth = new JLabel("数据起始月份(包含)：");
    JLabel labExpEndMonth = new JLabel("数据终止月份(不包含)：");
    JLabel labExpStartDate = new JLabel("数据起始日期(包含)：");
    JLabel labExpEndDate = new JLabel("数据终止日期(不包含)：");
    JLabel labExpStartYear = new JLabel("数据起始年份(包含)：");
    JLabel labExpEndYear = new JLabel("数据终止年份(不包含)：");
    // 数值类
    public static JTextField textExpAddr = new JTextField(20);
    public static JTextField textExpUserId = new JTextField(20);
    public static JTextField textExpFile = new JTextField(20);
    public static JTextField textExpLog = new JTextField(20);
    public static JTextField textExpBat = new JTextField(20);
    public static JTextField textExpMaxSize = new JTextField(20);
    public static JTextField textExpStartMonth = new JTextField(20);
    public static JTextField textExpEndMonth = new JTextField(20);
    public static JTextField textExpStartDate = new JTextField(20);
    public static JTextField textExpEndDate = new JTextField(20);
    public static JTextField textExpStartYear = new JTextField(20);
    public static JTextField textExpEndYear = new JTextField(20);
    // 日期选择器--后期需更改月份选择
    DateChooser dateChooser1 = DateChooser.getInstance("yyyy-MM-dd");
    DateChooser dateChooser2 = DateChooser.getInstance("yyyy-MM-dd");
    DateChooser dateChooser3 = DateChooser.getInstance("yyyy-MM");
    DateChooser dateChooser4 = DateChooser.getInstance("yyyy-MM");
    DateChooser dateChooser5 = DateChooser.getInstance("yyyy");
    DateChooser dateChooser6 = DateChooser.getInstance("yyyy");
    
    // 第一步的按钮
    JButton jbtnSetDefault_en_1 = new JButton("保存");
    JButton jbtnReSet_en_1 = new JButton("重置");
    JButton jbtnConnTest_en_1 = new JButton("测试连通性");
    JButton jbtnNext_en_1 = new JButton("下一步");

    JButton jbtnReSet_en_2 = new JButton("重置");
    JButton jbtnBack_en_2 = new JButton("返回");
    JButton jbtnFinish_en_2 = new JButton("完成");

    JButton jbtnBackHome_en_3 = new JButton("返回第一页");
    JButton jbtnBack_en_3 = new JButton("返回");

    // 选项卡中部分
    // 使用JTable查看展示效果
    static JTable jt = new JTable();
    // 选项卡下部分
    // 按钮类
    JLabel jlMaxSize = new JLabel(TransferSizeUtil.Long2Str(Conf4Exp.MAXBYTE));
    JButton jbExpAll = new JButton("全部导出");

    // 选项卡2
    // 面板分隔类
    JPanel tabImpParamPanel = new JPanel();
    JPanel tabImpParamPanelS1U = new JPanel(new GridLayout(2, 4));
    JPanel tabImpParamPanelS1D = new JPanel();
    JPanel tabImpParamPanelS2U = new JPanel(new GridLayout(3, 4));
    JPanel tabImpParamPanelS2D = new JPanel();
    JPanel tabImpParamPanelS3U = new JPanel(new GridLayout(2, 4));
    JPanel tabImpParamPanelS3D = new JPanel();
    JPanel tabImpMainPanel = new JPanel(new GridLayout(1, 1));
    JPanel tabImpStatusPanel = new JPanel(new GridLayout(1, 3));
    // JPanel jpImpNorth = new JPanel(new GridLayout(2, 4));
    // JPanel jpImpCenter = new JPanel(new GridLayout(1, 1));
    // JPanel jpImpSouth = new JPanel(new GridLayout(1, 3));

    // 选项卡2上部分
    // 参数类JLable和JTextField
    JLabel jlImpAddr = new JLabel("数据库地址：");
    JLabel jlImpUserId = new JLabel("用户名/密码：");
    JLabel jlImpLog = new JLabel("日志文件夹：");
    JLabel jlImpBat = new JLabel("Bat文件夹：");
    JTextField textImpAddr = new JTextField("127.0.0.1/orcl", 20);
    JTextField textImpUserId = new JTextField("open3k/orcl", 20);
    JTextField textImpLog = new JTextField("", 20);
    JTextField textImpBat = new JTextField("", 20);

    JButton jbtnSetDefault_in_1 = new JButton("保存");
    JButton jbtnReSet_in_1 = new JButton("重置");
    JButton jbtnConnTest_in_1 = new JButton("测试连通性");
    JButton jbtnNext_in_1 = new JButton("下一步");

    JButton jbtnReSet_in_2 = new JButton("重置");
    JButton jbtnBack_in_2 = new JButton("返回");
    JButton jbtnFinish_in_2 = new JButton("完成");

    JButton jbtnSelectFile_in_3 = new JButton("选择文件");
    JButton jbtnSelectDir_in_3 = new JButton("选择文件夹");
    JButton jbtnBackHome_in_3 = new JButton("返回第一页");
    JButton jbtnBack_in_3 = new JButton("返回");

    // 选项卡2中部分
    // 使用JTable查看展示效果
    static JTable jtImp = new JTable();
    // 选项卡2下部分
    // 按钮类
    JButton jbImpAll = new JButton("全部导入");

    public DataMigrationTool() {

        // 初始化菜单栏
        initMenu();

        dateChooser1.register(textExpStartDate);
        dateChooser2.register(textExpEndDate);
        dateChooser3.register(textExpStartMonth);
        dateChooser4.register(textExpEndMonth);
        dateChooser5.register(textExpStartYear);
        dateChooser6.register(textExpEndYear);

        // 获取tabExp上的各个面板，包括param，main，status
        JPanel tabExpParamJPanel = getTabExpParamJPanel(1);
        tabExp.add(tabExpParamJPanel);
        JPanel tabExpMainJPanel = getTabExpMainJPanel();
        tabExp.add(tabExpMainJPanel);
        JPanel tabExpStatusJPanel = getTabExpStatusJPanel();
        tabExp.add(tabExpStatusJPanel);

        // 获取tabImp上的各个面板，包括param，main，status
        JPanel tabImpParamJPanel = getTabImpParamJPanel(1);
        tabImp.add(tabImpParamJPanel);
        JPanel tabImpMainJPanel = getTabImpMainJPanel();
        tabImp.add(tabImpMainJPanel);
        JPanel tabImpStatusJPanel = getTabImpStatusJPanel();
        tabImp.add(tabImpStatusJPanel);

        // 将选项卡添加到JFrame中
        tabExp.setLayout(new BoxLayout(tabExp, BoxLayout.Y_AXIS));
        tabImp.setLayout(new BoxLayout(tabImp, BoxLayout.Y_AXIS));
        tabPane.addTab("数据导出", tabExp);
        tabPane.addTab("数据导入", tabImp);
        tabPane.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        add(tabPane);

        // 添加事件监听器
        menuItemConn.addActionListener(new JmiConnListener());
        menuItemConnTest.addActionListener(new JmiConnTestListener());
        menuItemAddFile.addActionListener(new JmiAddFileListener());
        menuItemAddDir.addActionListener(new JmiAddDirListener());
        menuItemAboutUs.addActionListener(new JmiAboutListener());
        menuItemContact.addActionListener(new JmiContactListener());
        tabPane.addChangeListener(new JtpListener());
        jbExpAll.addActionListener(new JbExpAllListener());
        jbImpAll.addActionListener(new JbImpAllListener());
        textExpMaxSize.addMouseListener(new MaxSizeListener());
        jbtnNext_en_1.addActionListener(new Jbtn_en_1_NextListener());
        jbtnConnTest_en_1.addActionListener(new Jbtn_en_1_ConnTestListener());
        jbtnSetDefault_en_1
                .addActionListener(new Jbtn_en_1_SetDefaultListener());
        jbtnReSet_en_1.addActionListener(new Jbtn_en_1_ReSetListener());

        jbtnReSet_en_2.addActionListener(new Jbtn_en_2_ReSetListener());
        jbtnBack_en_2.addActionListener(new Jbtn_en_2_BackListener());
        jbtnFinish_en_2.addActionListener(new Jbtn_en_2_FinishListener());

        jbtnBackHome_en_3.addActionListener(new Jbtn_en_2_BackListener());
        jbtnBack_en_3.addActionListener(new Jbtn_en_1_NextListener());

        jbtnNext_in_1.addActionListener(new Jbtn_in_1_NextListener());
        jbtnConnTest_in_1.addActionListener(new Jbtn_in_1_ConnTestListener());
        jbtnSetDefault_in_1
                .addActionListener(new Jbtn_in_1_SetDefaultListener());
        jbtnReSet_in_1.addActionListener(new Jbtn_in_1_ReSetListener());

        jbtnReSet_in_2.addActionListener(new Jbtn_in_2_ReSetListener());
        jbtnBack_in_2.addActionListener(new Jbtn_in_2_BackListener());
        jbtnFinish_in_2.addActionListener(new Jbtn_in_2_FinishListener());

        jbtnBackHome_in_3.addActionListener(new Jbtn_in_2_BackListener());
        jbtnBack_in_3.addActionListener(new Jbtn_in_1_NextListener());
        jbtnSelectFile_in_3.addActionListener(new JmiAddFileListener());
        jbtnSelectDir_in_3.addActionListener(new JmiAddDirListener());

        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    }

    private JPanel getTabImpStatusJPanel() {
        // 选项卡2下添加
        tabImpStatusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
                10));
        tabImpStatusPanel.add(new JComponent() {
        });
        tabImpStatusPanel.add(new JComponent() {
        });
        tabImpStatusPanel.add(jbImpAll);
        return tabImpStatusPanel;
    }

    private JPanel getTabImpMainJPanel() {
        // 选项卡2中添加
        DefaultTableModel dtmImp = new DefaultTableModel();
        dtmImp.setDataVector(null, tableHeadImpStr);
        jtImp = new JTable(dtmImp) {
            public JToolTip createToolTip() {
                return new CellToolTip();
            }
        };
        jtImp.setRowHeight(30);
        jtImp.setEnabled(false);
        tabImpMainPanel.add(new JScrollPane(jtImp));
        tabImpMainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
                10));
        return tabImpMainPanel;
    }

    private JPanel getTabImpParamJPanel(int step) {
        // 先移除再重新添加
        tabImpParamPanel.removeAll();
        // 选项卡1上添加
        if (step == 1) {

            tabImpParamPanelS1U.removeAll();
            tabImpParamPanelS1D.removeAll();

            tabImpParamPanelS1U.add(jlImpAddr);
            textImpAddr.setText(Conf4Imp.ADDR);
            tabImpParamPanelS1U.add(textImpAddr);

            tabImpParamPanelS1U.add(jlImpUserId);
            textImpUserId.setText(Conf4Imp.USERID);
            tabImpParamPanelS1U.add(textImpUserId);

            tabImpParamPanelS1U.add(jlImpLog);
            textImpLog.setText(Conf4Imp.LOG);
            tabImpParamPanelS1U.add(textImpLog);

            tabImpParamPanelS1U.add(jlImpBat);
            textImpBat.setText(Conf4Imp.BAT);
            tabImpParamPanelS1U.add(textImpBat);

            Border jpNorthb2 = BorderFactory.createEmptyBorder(10, 10, 0, 10);
            Border jpNorthb1 = BorderFactory.createTitledBorder("请设置导入参数");
            tabImpParamPanelS1U.setBorder(BorderFactory.createCompoundBorder(
                    jpNorthb2, jpNorthb1));

            FlowLayout layout0 = new FlowLayout(FlowLayout.RIGHT, 15, 3);
            tabImpParamPanelS1D.setLayout(layout0);
            tabImpParamPanelS1D.add(jbtnSetDefault_in_1);
            tabImpParamPanelS1D.add(jbtnReSet_in_1);
            tabImpParamPanelS1D.add(jbtnConnTest_in_1);
            tabImpParamPanelS1D.add(jbtnNext_in_1);
            tabImpParamPanelS1D.setBorder(BorderFactory.createEmptyBorder(0,
                    10, 0, 10));

            BoxLayout layout1 = new BoxLayout(tabImpParamPanel,
                    BoxLayout.Y_AXIS);
            tabImpParamPanel.setLayout(layout1);
            tabImpParamPanel.add(tabImpParamPanelS1U);
            tabImpParamPanel.add(tabImpParamPanelS1D);
        } else if (step == 2) {

            tabImpParamPanelS2U.removeAll();
            tabImpParamPanelS2D.removeAll();

            List<JPanel> jPanelList = initImpParamRadioButton();
            GridBagLayout gridbag = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            tabImpParamPanelS2U.setLayout(gridbag);

            Border jpNorthb2 = BorderFactory.createEmptyBorder(10, 10, 0, 10);
            Border jpNorthb1 = BorderFactory.createTitledBorder("请设置导入参数");
            tabImpParamPanelS2U.setBorder(BorderFactory.createCompoundBorder(
                    jpNorthb2, jpNorthb1));

            c.anchor = GridBagConstraints.NORTHWEST;
            int i = 0;
            for (JPanel jPanel2 : jPanelList) {
                if (i == 3 || i == 7) {
                    c.gridwidth = GridBagConstraints.REMAINDER;
                }
                if (i == 8 || i == 9) {
                    c.gridwidth = 2;
                }
                gridbag.setConstraints(jPanel2, c);
                tabImpParamPanelS2U.add(jPanel2);
                c.gridwidth = 1;
                i++;
            }

            FlowLayout layout0 = new FlowLayout(FlowLayout.RIGHT, 15, 3);
            tabImpParamPanelS2D.setLayout(layout0);
            tabImpParamPanelS2D.add(jbtnReSet_in_2);
            tabImpParamPanelS2D.add(jbtnBack_in_2);
            tabImpParamPanelS2D.add(jbtnFinish_in_2);
            tabImpParamPanelS2D.setBorder(BorderFactory.createEmptyBorder(0,
                    10, 0, 10));

            tabImpParamPanel.add(tabImpParamPanelS2U);
            tabImpParamPanel.add(tabImpParamPanelS2D);

        } else if (step == 3) {
            tabImpParamPanelS3U.removeAll();
            tabImpParamPanelS3D.removeAll();

            JTextField textImpAddr = new JTextField(Conf4Imp.ADDR, 20);
            JTextField textImpUserId = new JTextField(Conf4Imp.USERID, 20);
            JTextField textImpLog = new JTextField(Conf4Imp.LOG, 20);
            JTextField textImpBat = new JTextField(Conf4Imp.BAT, 20);

            textImpAddr.setEditable(false);
            textImpUserId.setEditable(false);
            textImpLog.setEditable(false);
            textImpBat.setEditable(false);

            tabImpParamPanelS3U.add(jlImpAddr);
            tabImpParamPanelS3U.add(textImpAddr);

            tabImpParamPanelS3U.add(jlImpUserId);
            tabImpParamPanelS3U.add(textImpUserId);

            tabImpParamPanelS3U.add(jlImpLog);
            tabImpParamPanelS3U.add(textImpLog);

            tabImpParamPanelS3U.add(jlImpBat);
            tabImpParamPanelS3U.add(textImpBat);

            Border jpNorthb2 = BorderFactory.createEmptyBorder(10, 10, 0, 10);
            Border jpNorthb1 = BorderFactory.createTitledBorder("请设置导出参数");
            tabImpParamPanelS3U.setBorder(BorderFactory.createCompoundBorder(
                    jpNorthb2, jpNorthb1));

            FlowLayout layout0 = new FlowLayout(FlowLayout.RIGHT, 15, 3);
            tabImpParamPanelS3D.setLayout(layout0);
            tabImpParamPanelS3D.add(jbtnSelectFile_in_3);
            tabImpParamPanelS3D.add(jbtnSelectDir_in_3);
            tabImpParamPanelS3D.add(jbtnBackHome_in_3);
            tabImpParamPanelS3D.add(jbtnBack_in_3);
            tabImpParamPanelS3D.setBorder(BorderFactory.createEmptyBorder(0,
                    10, 0, 10));

            BoxLayout layout1 = new BoxLayout(tabImpParamPanel,
                    BoxLayout.Y_AXIS);
            tabImpParamPanel.setLayout(layout1);
            tabImpParamPanel.add(tabImpParamPanelS3U);
            tabImpParamPanel.add(tabImpParamPanelS3D);
        }
        return tabImpParamPanel;

    }

    private JPanel getTabExpStatusJPanel() {
        // 选项卡1下添加
        tabExpStatusPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
                10));
        tabExpStatusPanel.add(jlMaxSize);
        tabExpStatusPanel.add(new JComponent() {
        });
        tabExpStatusPanel.add(jbExpAll);
        return tabExpStatusPanel;
    }

    private JPanel getTabExpMainJPanel() {
        // 选项卡1中添加
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.setDataVector(null, tableHeadStr);
        jt = new JTable(dtm) {
            public JToolTip createToolTip() {
                return new CellToolTip();
            }
        };
        jt.setRowHeight(30);
        jt.setEnabled(false);
        tabExpMainPanel.add(new JScrollPane(jt));
        tabExpMainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10,
                10));
        return tabExpMainPanel;
    }

    private JPanel getTabExpParamJPanel(int step) {
        // 先移除再重新添加
        tabExpParamPanel.removeAll();
        // 选项卡1上添加
        if (step == 1) {
            tabExpParamPanelS1U.removeAll();
            tabExpParamPanelS1D.removeAll();

            textExpAddr.setText(Conf4Exp.ADDR);
            textExpUserId.setText(Conf4Exp.USERID);
            textExpFile.setText(Conf4Exp.FILE);
            textExpLog.setText(Conf4Exp.LOG);
            textExpBat.setText(Conf4Exp.BAT);
            textExpMaxSize.setText(Conf4Exp.MAXSIZE);
            textExpStartMonth.setText(Conf4Exp.STARTMONTH);
            textExpEndMonth.setText(Conf4Exp.ENDMONTH);
            textExpStartDate.setText(Conf4Exp.STARTDATE);
            textExpEndDate.setText(Conf4Exp.ENDDATE);
            textExpStartYear.setText(Conf4Exp.STARTYEAR);
            textExpEndYear.setText(Conf4Exp.ENDYEAR);

            tabExpParamPanelS1U.add(labExpAddr);
            tabExpParamPanelS1U.add(textExpAddr);
            tabExpParamPanelS1U.add(labExpUserId);
            tabExpParamPanelS1U.add(textExpUserId);
            tabExpParamPanelS1U.add(labExpFile);
            tabExpParamPanelS1U.add(textExpFile);
            tabExpParamPanelS1U.add(labExpLog);
            tabExpParamPanelS1U.add(textExpLog);
            tabExpParamPanelS1U.add(labExpBat);
            tabExpParamPanelS1U.add(textExpBat);
            tabExpParamPanelS1U.add(labExpMaxSize);
            tabExpParamPanelS1U.add(textExpMaxSize);
            tabExpParamPanelS1U.add(labExpStartMonth);
            tabExpParamPanelS1U.add(textExpStartMonth);
            tabExpParamPanelS1U.add(labExpEndMonth);
            tabExpParamPanelS1U.add(textExpEndMonth);
            tabExpParamPanelS1U.add(labExpStartDate);
            tabExpParamPanelS1U.add(textExpStartDate);
            tabExpParamPanelS1U.add(labExpEndDate);
            tabExpParamPanelS1U.add(textExpEndDate);
            tabExpParamPanelS1U.add(labExpStartYear);
            tabExpParamPanelS1U.add(textExpStartYear);
            tabExpParamPanelS1U.add(labExpEndYear);
            tabExpParamPanelS1U.add(textExpEndYear);

            Border jpNorthb2 = BorderFactory.createEmptyBorder(10, 10, 0, 10);
            Border jpNorthb1 = BorderFactory.createTitledBorder("请设置导出参数");
            tabExpParamPanelS1U.setBorder(BorderFactory.createCompoundBorder(
                    jpNorthb2, jpNorthb1));

            FlowLayout layout0 = new FlowLayout(FlowLayout.RIGHT, 15, 3);
            tabExpParamPanelS1D.setLayout(layout0);
            tabExpParamPanelS1D.add(jbtnSetDefault_en_1);
            tabExpParamPanelS1D.add(jbtnReSet_en_1);
            tabExpParamPanelS1D.add(jbtnConnTest_en_1);
            tabExpParamPanelS1D.add(jbtnNext_en_1);
            tabExpParamPanelS1D.setBorder(BorderFactory.createEmptyBorder(0,
                    10, 0, 10));

            BoxLayout layout1 = new BoxLayout(tabExpParamPanel,
                    BoxLayout.Y_AXIS);
            tabExpParamPanel.setLayout(layout1);
            tabExpParamPanel.add(tabExpParamPanelS1U);
            tabExpParamPanel.add(tabExpParamPanelS1D);
        } else if (step == 2) {

            tabExpParamPanelS2U.removeAll();
            tabExpParamPanelS2D.removeAll();

            List<JPanel> jPanelList = initParamRadioButton();
            GridBagLayout gridbag = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            tabExpParamPanelS2U.setLayout(gridbag);

            Border jpNorthb2 = BorderFactory.createEmptyBorder(10, 10, 0, 10);
            Border jpNorthb1 = BorderFactory.createTitledBorder("请设置导出参数");
            tabExpParamPanelS2U.setBorder(BorderFactory.createCompoundBorder(
                    jpNorthb2, jpNorthb1));

            c.anchor = GridBagConstraints.NORTHWEST;
            int i = 0;
            for (JPanel jPanel2 : jPanelList) {
                if (i == 3 || i == 7) {
                    c.gridwidth = GridBagConstraints.REMAINDER;
                }
                if (i == 8 || i == 9) {
                    c.gridwidth = 2;
                }
                gridbag.setConstraints(jPanel2, c);
                tabExpParamPanelS2U.add(jPanel2);
                c.gridwidth = 1;
                i++;
            }

            FlowLayout layout0 = new FlowLayout(FlowLayout.RIGHT, 15, 3);
            tabExpParamPanelS2D.setLayout(layout0);
            tabExpParamPanelS2D.add(jbtnReSet_en_2);
            tabExpParamPanelS2D.add(jbtnBack_en_2);
            tabExpParamPanelS2D.add(jbtnFinish_en_2);
            tabExpParamPanelS2D.setBorder(BorderFactory.createEmptyBorder(0,
                    10, 0, 10));

            tabExpParamPanel.add(tabExpParamPanelS2U);
            tabExpParamPanel.add(tabExpParamPanelS2D);

        } else if (step == 3) {
            tabExpParamPanelS3U.removeAll();
            tabExpParamPanelS3D.removeAll();

            JTextField textExpAddr = new JTextField(Conf4Exp.ADDR, 20);
            JTextField textExpUserId = new JTextField(Conf4Exp.USERID, 20);
            JTextField textExpFile = new JTextField(Conf4Exp.FILE, 20);
            JTextField textExpLog = new JTextField(Conf4Exp.LOG, 20);
            JTextField textExpBat = new JTextField(Conf4Exp.BAT, 20);
            JTextField textExpMaxSize = new JTextField(Conf4Exp.MAXSIZE, 20);
            JTextField textExpStartMonth = new JTextField(Conf4Exp.STARTMONTH,
                    20);
            JTextField textExpEndMonth = new JTextField(Conf4Exp.ENDMONTH, 20);
            JTextField textExpStartDate = new JTextField(Conf4Exp.STARTDATE, 20);
            JTextField textExpEndDate = new JTextField(Conf4Exp.ENDDATE, 20);
            JTextField textExpStartYear = new JTextField(Conf4Exp.STARTYEAR, 20);
            JTextField textExpEndYear = new JTextField(Conf4Exp.ENDYEAR, 20);

            textExpAddr.setEditable(false);
            textExpUserId.setEditable(false);
            textExpFile.setEditable(false);
            textExpLog.setEditable(false);
            textExpBat.setEditable(false);
            textExpMaxSize.setEditable(false);
            textExpStartMonth.setEditable(false);
            textExpEndMonth.setEditable(false);
            textExpStartDate.setEditable(false);
            textExpEndDate.setEditable(false);
            textExpStartYear.setEditable(false);
            textExpEndYear.setEditable(false);

            tabExpParamPanelS3U.add(labExpAddr);
            tabExpParamPanelS3U.add(textExpAddr);
            tabExpParamPanelS3U.add(labExpUserId);
            tabExpParamPanelS3U.add(textExpUserId);
            tabExpParamPanelS3U.add(labExpFile);
            tabExpParamPanelS3U.add(textExpFile);
            tabExpParamPanelS3U.add(labExpLog);
            tabExpParamPanelS3U.add(textExpLog);
            tabExpParamPanelS3U.add(labExpBat);
            tabExpParamPanelS3U.add(textExpBat);
            tabExpParamPanelS3U.add(labExpMaxSize);
            tabExpParamPanelS3U.add(textExpMaxSize);
            tabExpParamPanelS3U.add(labExpStartMonth);
            tabExpParamPanelS3U.add(textExpStartMonth);
            tabExpParamPanelS3U.add(labExpEndMonth);
            tabExpParamPanelS3U.add(textExpEndMonth);
            tabExpParamPanelS3U.add(labExpStartDate);
            tabExpParamPanelS3U.add(textExpStartDate);
            tabExpParamPanelS3U.add(labExpEndDate);
            tabExpParamPanelS3U.add(textExpEndDate);
            tabExpParamPanelS3U.add(labExpStartYear);
            tabExpParamPanelS3U.add(textExpStartYear);
            tabExpParamPanelS3U.add(labExpEndYear);
            tabExpParamPanelS3U.add(textExpEndYear);

            Border jpNorthb2 = BorderFactory.createEmptyBorder(10, 10, 0, 10);
            Border jpNorthb1 = BorderFactory.createTitledBorder("请设置导出参数");
            tabExpParamPanelS3U.setBorder(BorderFactory.createCompoundBorder(
                    jpNorthb2, jpNorthb1));

            FlowLayout layout0 = new FlowLayout(FlowLayout.RIGHT, 15, 3);
            tabExpParamPanelS3D.setLayout(layout0);
            tabExpParamPanelS3D.add(jbtnBackHome_en_3);
            tabExpParamPanelS3D.add(jbtnBack_en_3);
            tabExpParamPanelS3D.setBorder(BorderFactory.createEmptyBorder(0,
                    10, 0, 10));

            BoxLayout layout1 = new BoxLayout(tabExpParamPanel,
                    BoxLayout.Y_AXIS);
            tabExpParamPanel.setLayout(layout1);
            tabExpParamPanel.add(tabExpParamPanelS3U);
            tabExpParamPanel.add(tabExpParamPanelS3D);
        }
        return tabExpParamPanel;
    }

    @SuppressWarnings("static-access")
    private List<JPanel> initParamRadioButton() {

        List<JPanel> jpanelList = Lists.newArrayList();
        Map<String, String> expParamMap = Maps.newLinkedHashMap();
        expParamMap.put("compress", Conf4Exp.COMPRESS);
        expParamMap.put("consistent", Conf4Exp.CONSISTENT);
        expParamMap.put("grants", Conf4Exp.GRANTS);
        expParamMap.put("indexes", Conf4Exp.INDEXES);
        expParamMap.put("rows", Conf4Exp.ROWS);
        expParamMap.put("triggers", Conf4Exp.TRIGGERS);
        expParamMap.put("constraints", Conf4Exp.CONSTRAINTS);
        expParamMap.put("buffer", Conf4Exp.BUFFER);
        expParamMap.put("other", Conf4Exp.OTHER);
        for (Entry<String, String> entry : expParamMap.entrySet()) {
            JPanel jPanel = new JPanel();
            jPanel.add(new JLabel(entry.getKey()));
            if ("buffer".equals(entry.getKey())) {
                JTextField text = new JTextField(entry.getValue(), 20);
                jPanel.add(text);
                Document dt = text.getDocument();
                dt.addDocumentListener(new TextBufferListener());
                jpanelList.add(new JPanel());
            } else if ("other".equals(entry.getKey())) {
                JTextField text = new JTextField(entry.getValue(), 20);
                jPanel.add(text);
                Document dt = text.getDocument();
                dt.addDocumentListener(new TextListener());
            } else {
                JRadioButton radioButton1 = new JRadioButton("是");
                radioButton1.setName(entry.getKey());
                JRadioButton radioButton2 = new JRadioButton("否");
                radioButton2.setName(entry.getKey());
                radioButton1.addActionListener(new RadioListener());
                radioButton2.addActionListener(new RadioListener());
                ButtonGroup group = new ButtonGroup();// 创建单选按钮组
                group.add(radioButton1);// 将radioButton1增加到单选按钮组中
                group.add(radioButton2);// 将radioButton2增加到单选按钮组中
                if ("Y".equals(entry.getValue())) {
                    radioButton1.setSelected(true);
                } else {
                    radioButton2.setSelected(true);
                }
                // jPanel.add(radioButton1);
                // jPanel.add(radioButton2);

                JCheckBox cb = new JCheckBox();
                cb.setSelected("Y".equals(entry.getValue()));
                cb.setName(entry.getKey());
                cb.setHorizontalTextPosition(cb.RIGHT);
                cb.addActionListener(new CheckBoxListener());
                jPanel.add(cb);

            }
            jpanelList.add(jPanel);
        }

        return jpanelList;
    }

    @SuppressWarnings("static-access")
    private List<JPanel> initImpParamRadioButton() {

        List<JPanel> jpanelList = Lists.newArrayList();
        Map<String, String> impParamMap = Maps.newLinkedHashMap();
        impParamMap.put("full", Conf4Imp.FULL);
        impParamMap.put("grants", Conf4Imp.GRANTS);
        impParamMap.put("indexes", Conf4Imp.INDEXES);
        impParamMap.put("rows", Conf4Imp.ROWS);
        impParamMap.put("constraints", Conf4Imp.CONSTRAINTS);
        impParamMap.put("ignore", Conf4Imp.IGNORE);
        impParamMap.put("buffer", Conf4Imp.BUFFER);
        impParamMap.put("other", Conf4Imp.OTHER);
        for (Entry<String, String> entry : impParamMap.entrySet()) {
            JPanel jPanel = new JPanel();
            jPanel.add(new JLabel(entry.getKey()));
            if ("buffer".equals(entry.getKey())) {
                JTextField text = new JTextField(entry.getValue(), 20);
                jPanel.add(text);
                Document dt = text.getDocument();
                dt.addDocumentListener(new ImpTextBufferListener());
                jpanelList.add(new JPanel());
                jpanelList.add(new JPanel());
            } else if ("other".equals(entry.getKey())) {
                JTextField text = new JTextField(entry.getValue(), 20);
                jPanel.add(text);
                Document dt = text.getDocument();
                dt.addDocumentListener(new ImpTextListener());
            } else {
                JRadioButton radioButton1 = new JRadioButton("是");
                radioButton1.setName(entry.getKey());
                JRadioButton radioButton2 = new JRadioButton("否");
                radioButton2.setName(entry.getKey());
                radioButton1.addActionListener(new ImpRadioListener());
                radioButton2.addActionListener(new ImpRadioListener());
                ButtonGroup group = new ButtonGroup();// 创建单选按钮组
                group.add(radioButton1);// 将radioButton1增加到单选按钮组中
                group.add(radioButton2);// 将radioButton2增加到单选按钮组中
                if ("Y".equals(entry.getValue())) {
                    radioButton1.setSelected(true);
                } else {
                    radioButton2.setSelected(true);
                }
                // jPanel.add(radioButton1);
                // jPanel.add(radioButton2);

                JCheckBox cb = new JCheckBox();
                cb.setSelected("Y".equals(entry.getValue()));
                cb.setName(entry.getKey());
                cb.setHorizontalTextPosition(cb.RIGHT);
                cb.addActionListener(new CheckBoxImpListener());
                jPanel.add(cb);
            }
            jpanelList.add(jPanel);
        }

        return jpanelList;
    }

    private void initMenu() {
        menuItemConnTest.setEnabled(false);
        menuItemAddFile.setEnabled(false);
        menuItemAddDir.setEnabled(false);
        // menuExp.add(menuItemSize);
        // menuExp.add(menuItemConn);
        // menuImp.add(menuItemConnTest);
        // menuImp.add(menuItemAddFile);
        // menuImp.add(menuItemAddDir);
        menuHelp.add(menuItemAboutUs);
        menuHelp.add(menuItemContact);
        menuBar.add(menuExp);
        menuBar.add(menuImp);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);
    }

    /**
     * 用户输入参数验证
     * 
     * @param expProp
     * @param connType
     * @return
     */
    protected static boolean validateParam(boolean isExp, int mode) {
        ParamError paramError = null;
        if (isExp) {
            paramError = ValidateUtil.validateParam(mode);
        } else {
            paramError = ValidateUtil.validateImpParam(mode);
        }

        switch (paramError) {
        case PARAM_ERROR_URL:
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    DialogUtil.message("数据库地址格式不正确！", "用户参数检查提示", 0);
                }
            });
            break;
        case PARAM_ERROR_USER:
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    DialogUtil.message("用户名不能为空！", "用户参数检查提示", 0);
                }
            });
            break;
        case PARAM_ERROR_EXPPATH:
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    DialogUtil.message("文件夹不存在或者创建失败！", "用户参数检查提示", 0);
                }
            });
            break;
        case PARAM_ERROR_STARTTIME:
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    DialogUtil.message("起始时间格式不正确！", "用户参数检查提示", 0);
                }
            });
            break;
        case PARAM_ERROR_ENDTIME:
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    DialogUtil.message("终止时间格式不正确！", "用户参数检查提示", 0);
                }
            });
            break;
        case PARAM_ERROR_CONN:
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    DialogUtil.message("不能连接数据库，请查看连接参数或网络状况！", "用户参数检查提示", 0);
                }
            });
            break;
        case PARAM_PASS:
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    DialogUtil.message("参数检查通过，且数据库连接成功！", "用户参数检查提示", 1);
                }
            });
            return true;
        default:
            break;
        }
        return false;

    }

    /**
     * 导出界面的连接数据库并获取分区结构
     * 
     * @param expProp
     * @throws InterruptedException
     */
    public static void connDatabase() throws InterruptedException {

        // 用户输入参数验证
        if (!validateParam(true, 1))
            return;

        // 每次调用连接方法时，首先清空JTable
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DefaultTableModel tableModel = (DefaultTableModel) jt
                        .getModel();
                tableModel.setRowCount(0);
                jt.revalidate();
            }
        });

        final List<TmpExpData> expParamList = Lists.newArrayList();

        // 为空表自动分配空间
        DatabaseUtil.AllocateExtent();

        // 获取并处理普通表
        List<DataValue> dataValuesNormalList = DatabaseUtil.fetchNormalTables();
        Map<String, Long> dataValuesNormalMap = Maps.newHashMap();
        for (DataValue dataValue : dataValuesNormalList) {
            dataValuesNormalMap.put(dataValue.getTableName(),
                    dataValue.getTableBytes());
        }
        List<String> stringNormalList = new ArrayList<String>();
        for (int i = 0; i < dataValuesNormalList.size(); i++) {
            String tableName = dataValuesNormalList.get(i).getTableName();
            if (Conf4Exp.MAXBYTE > 0
                    && dataValuesNormalList.get(i).getTableBytes() >= Conf4Exp.MAXBYTE) {
                expParamList.add(new TmpExpData("普通表_大容量表", tableName,
                        dataValuesNormalList.get(i).getTableBytes()));
                continue;
            }
            stringNormalList.add(tableName);
        }
        if (stringNormalList.size() > 0) {
            // 排序
            Collections.sort(stringNormalList);
            // 为了避免过多的表名导致导入导出语句过长，如果有过多的表则分开处理
            // 设置表名最长的长度
            List<List<String>> parList = Lists.partition(stringNormalList,
                    Conf.TABLESNUM);
            for (int i = 0; i < parList.size(); i++) {
                Long bytesCount = 0l;
                for (String tableName : parList.get(i)) {
                    bytesCount += dataValuesNormalMap.get(tableName);
                }
                expParamList.add(new TmpExpData("普通表" + i, StringUtils.join(
                        parList.get(i), ","), bytesCount));
            }
        }

        // 获取并处理带有lob字段表
        List<DataValue> dataValuesLobList = DatabaseUtil.fetchLobTables();
        Map<String, Long> dataValuesLobMap = Maps.newHashMap();
        for (DataValue dataValue : dataValuesLobList) {
            dataValuesLobMap.put(dataValue.getTableName(),
                    dataValue.getTableBytes());
        }
        List<String> stringLobList = new ArrayList<String>();
        for (int i = 0; i < dataValuesLobList.size(); i++) {
            String tableName = dataValuesLobList.get(i).getTableName();
            if (Conf4Exp.MAXBYTE > 0
                    && dataValuesLobList.get(i).getTableBytes() >= Conf4Exp.MAXBYTE) {
                expParamList.add(new TmpExpData("大对象表_大容量表", tableName,
                        dataValuesLobList.get(i).getTableBytes()));
                continue;
            }
            stringLobList.add(tableName);
        }
        if (stringLobList.size() > 0) {
            // 排序
            Collections.sort(stringLobList);
            // 为了避免过多的表名导致导入导出语句过长，如果有过多的表则分开处理
            // 设置表名最长的长度
            List<List<String>> parList = Lists.partition(stringLobList,
                    Conf.TABLESNUM);
            for (int i = 0; i < parList.size(); i++) {
                Long bytesCount = 0l;
                for (String tableName : parList.get(i)) {
                    bytesCount += dataValuesLobMap.get(tableName);
                }
                expParamList.add(new TmpExpData("大对象表" + i, StringUtils.join(
                        parList.get(i), ","), bytesCount));
            }
        }

        // 获取并处理分区表
        List<DataValue> dataValuesParList = DatabaseUtil.fetchParTables();
        Map<String, LinkedHashMap<String, Long>> dataValuesParMap = Maps
                .newLinkedHashMap();
        for (DataValue dataValue : dataValuesParList) {
            LinkedHashMap<String, Long> tmpMap = null;
            if (dataValuesParMap.containsKey(dataValue.getTableName())) {
                tmpMap = dataValuesParMap.get(dataValue.getTableName());
            } else {
                tmpMap = Maps.newLinkedHashMap();
                dataValuesParMap.put(dataValue.getTableName(), tmpMap);
            }
            tmpMap.put(dataValue.getParName(), dataValue.getTableBytes());
        }
        
        //建立一个list专门来存储按月分区表，一旦分区的数目超出了设定的值便向expParamList中存储一次
        List<String> parMonthList =Lists.newArrayList();
        Map<String,Long> parMonthMap =Maps.newHashMap();
        
        //建立一个list专门来存储按年分区表，一旦分区的数目超出了设定的值便向expParamList中存储一次
        List<String> parYearList =Lists.newArrayList();
        Map<String,Long> parYearMap =Maps.newHashMap();
        
        for (Entry<String, LinkedHashMap<String, Long>> entry : dataValuesParMap
                .entrySet()) {
            // 过滤有效的分区
            Map<String, Long> valueMap = entry.getValue();
            // 显示过滤后的分区字段结果
            String parName = "分区表" + entry.getKey();
            // 统计表名+分区名的List
            List<String> tableParName = Lists.newArrayList();
            // 记录分区类型：类型1->按月分区，类型2->按日分区，类型3->按年分区
            int parType = 0;
            // 总的大小
            Long bytesCount = 0l;
            int sum = 0;
            int endName = 0;
            boolean isNull = true;

            for (Entry<String, Long> entry1 : valueMap.entrySet()) {
            	parType = 0;
                // 验证分区前缀
                if (!entry1.getKey().startsWith("PAR")) {
                    parName += "_" + entry1.getKey() + "分区名格式错误";
                }
                String timeString = entry1.getKey().substring(3);             
                // 第一次验证成功后，即定义分区类型，便于后期对于导入导出语句文件数量和语句长度的调整。
                DateTimeFormatter formatDate = DateTimeFormat
                        .forPattern("yyyyMMdd");
                DateTimeFormatter formatMonth = DateTimeFormat
                        .forPattern("yyyyMM");
                DateTimeFormatter formatYear = DateTimeFormat
                        .forPattern("yyyy");
                DateTime dateTime = null;
                if (parType == 0) {
                    try {
                        dateTime = DateTime.parse(timeString, formatMonth);
                        parType = 1;
                    } catch (Exception e) {
                        try {
                            dateTime = DateTime.parse(timeString, formatDate);
                            parType = 2;
                        } catch (Exception e1) {
                        	try {
                                dateTime = DateTime.parse(timeString, formatYear);
                                parType = 3;
                            } catch (Exception e2) {
                                System.out.println("输入格式错误!请重新输入."+timeString);
                            }
                        }
                    }
                }
                /*
                else if (parType == 1) {
                    dateTime = DateTime.parse(timeString, formatMonth);
                } else if (parType == 2) {
                    dateTime = DateTime.parse(timeString, formatDate);
                } else if (parType == 3){
                	dateTime = DateTime.parse(timeString, formatYear);
                }
                */
                // 按月分区的分区表需要合并
                if (parType == 1) {
                    try {
                        if ((!dateTime.toDate().before(
                                DateUtils.parseDate(Conf4Exp.STARTMONTH,
                                        new String[] { "yyyy-MM" })))
                                && dateTime.toDate().before(
                                        DateUtils.parseDate(Conf4Exp.ENDMONTH,
                                                new String[] { "yyyy-MM" }))) {
                            tableParName.add(entry.getKey() + ":"
                                    + entry1.getKey());
                            parMonthList.add(entry.getKey() + ":"
                                    + entry1.getKey());
                            parMonthMap.put(entry.getKey() + ":"
                                    + entry1.getKey(), entry1.getValue());
                            bytesCount += entry1.getValue();
                            sum++;
                        }
                    } catch (ParseException e) {
                        parName += "_" + entry1.getKey() + "不能解析此分区";
                        e.printStackTrace();
                    }
                    
                    // 如果表的容量比较大，就不要合并了，判断依据是各个分区的容量大小和是否超出了设置的最大容量
                    // if (bytesCount > Conf4Exp.MAXBYTE) {
                    // Collections.sort(tableParName);
                    //
                    // expParamList.add(new TmpExpData(
                    // parName + "_" + endName, StringUtils.join(
                    // tableParName, ","), bytesCount));
                    // }
                }
                if (parType == 2) {
                    try {
                        if ((!dateTime.toDate().before(
                                DateUtils.parseDate(Conf4Exp.STARTDATE,
                                        new String[] { "yyyy-MM-dd" })))
                                && dateTime.toDate().before(
                                        DateUtils.parseDate(Conf4Exp.ENDDATE,
                                                new String[] { "yyyy-MM-dd" }))) {
                            tableParName.add(entry.getKey() + ":"
                                    + entry1.getKey());
                            bytesCount += entry1.getValue();
                            sum++;
                        }
                    } catch (ParseException e) {
                        parName += "_" + entry1.getKey() + "不能解析此分区";
                        e.printStackTrace();
                    }
                }
                // 按年分区的分区表需要合并
                if (parType == 3) {
                    try {
                        if ((!dateTime.toDate().before(
                                DateUtils.parseDate(Conf4Exp.STARTYEAR,
                                        new String[] { "yyyy" })))
                                && dateTime.toDate().before(
                                        DateUtils.parseDate(Conf4Exp.ENDYEAR,
                                                new String[] { "yyyy" }))) {
                            tableParName.add(entry.getKey() + ":"
                                    + entry1.getKey());
                            parYearList.add(entry.getKey() + ":"
                                    + entry1.getKey());
                            parYearMap.put(entry.getKey() + ":"
                                    + entry1.getKey(), entry1.getValue());
                            bytesCount += entry1.getValue();
                            sum++;
                        }
                    } catch (ParseException e) {
                        parName += "_" + entry1.getKey() + "不能解析此分区";
                        e.printStackTrace();
                    }
                    
                    // 如果表的容量比较大，就不要合并了，判断依据是各个分区的容量大小和是否超出了设置的最大容量
                    // if (bytesCount > Conf4Exp.MAXBYTE) {
                    // Collections.sort(tableParName);
                    //
                    // expParamList.add(new TmpExpData(
                    // parName + "_" + endName, StringUtils.join(
                    // tableParName, ","), bytesCount));
                    // }
                }

                if (sum >= Conf.TABLESNUM) {
                    Collections.sort(tableParName);
                    expParamList.add(new TmpExpData(parName + "_" + endName,
                            StringUtils.join(tableParName, ","), bytesCount));
                    endName++;
                    sum = 0;
                    tableParName.clear();
                    bytesCount = 0l;
                    isNull = false;
                }
                
            }
            // parName +="_"+parType;
            if (tableParName.size() == 0 && isNull == true) {
                tableParName.add(entry.getKey());
            }
            if(parType == 1){
                continue;
            }
            Collections.sort(tableParName);

            expParamList.add(new TmpExpData(parName + "_" + endName,
                    StringUtils.join(tableParName, ","), bytesCount));
        }
        //处理按月分区表
        if (parMonthList.size() > 0) {
            // 为了避免过多的表名导致导入导出语句过长，如果有过多的表则分开处理
            // 设置表名最长的长度
            List<List<String>> parList = Lists.partition(parMonthList,
                    Conf.TABLESNUM);
            for (int i = 0; i < parList.size(); i++) {
                Long bytesCount = 0l;
                for (String tableName : parList.get(i)) {
                    bytesCount += parMonthMap.get(tableName);
                }
                expParamList.add(new TmpExpData("分区表_月分区" + i, StringUtils.join(
                        parList.get(i), ","), bytesCount));
            }
        }
        
        
      //处理按年分区表
        if (parYearList.size() > 0) {
            // 为了避免过多的表名导致导入导出语句过长，如果有过多的表则分开处理
            // 设置表名最长的长度
            List<List<String>> parList = Lists.partition(parYearList,
                    Conf.TABLESNUM);
            for (int i = 0; i < parList.size(); i++) {
                Long bytesCount = 0l;
                for (String tableName : parList.get(i)) {
                    bytesCount += parYearMap.get(tableName);
                }
                expParamList.add(new TmpExpData("分区表_年分区" + i, StringUtils.join(
                        parList.get(i), ","), bytesCount));
            }
        }
        

        // 按行依次向图像中添加表
        final DefaultTableModel jtm = (DefaultTableModel) jt.getModel();
        final ButtonRendererListener renderer = new ButtonRendererListener(jt);
        final CellRenderer cellRenderer = new CellRenderer(jt);
        MouseListener[] mlArr = jt.getMouseListeners();
        if (mlArr.length == 2)
            jt.addMouseListener(renderer);

        for (final TmpExpData tmpExpData : expParamList) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // 处理为h类型
                    Long bytes = tmpExpData.getBytes();
                    String bytes1 = TransferSizeUtil.Long2Str(bytes);

                    jtm.addRow(new Object[] { tmpExpData.getType(),
                            tmpExpData.getTableName(), bytes1, "导出" });
                    jt.setOpaque(true);
                    // 注入渲染器
                    jt.getColumn(tableHeadStr[0]).setCellRenderer(cellRenderer);
                    jt.getColumn(tableHeadStr[1]).setCellRenderer(cellRenderer);
                    jt.getColumn(tableHeadStr[2]).setCellRenderer(cellRenderer);
                    jt.getColumn(tableHeadStr[3]).setCellRenderer(renderer);
                    jt.revalidate();
                }
            });
        }
    }

    public void fillImpTableCell(Map<String, String> rowMap) {

        final DefaultTableModel jtm = (DefaultTableModel) jtImp.getModel();
        // 每次调用连接方法时，首先清空JTable
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                jtm.setRowCount(0);
                jt.revalidate();
            }
        });
        final ButtonRendererListenerImp renderer = new ButtonRendererListenerImp(
                jtImp);
        final CellRendererImp cellRenderer = new CellRendererImp(jtImp);
        MouseListener[] mlArr = jtImp.getMouseListeners();
        if (mlArr.length == 2)
            jtImp.addMouseListener(renderer);
        for (final Entry<String, String> entryMap : rowMap.entrySet()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    jtm.addRow(new Object[] { entryMap.getValue(),
                            entryMap.getKey(), "导入" });
                    jtImp.setOpaque(true);
                    // 注入渲染器
                    jtImp.getColumn(tableHeadImpStr[0]).setCellRenderer(
                            cellRenderer);
                    jtImp.getColumn(tableHeadImpStr[1]).setCellRenderer(
                            cellRenderer);
                    jtImp.getColumn(tableHeadImpStr[2]).setCellRenderer(
                            renderer);
                    jtImp.revalidate();
                }
            });
        }
    }

    class JmiConnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                connDatabase();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    class Jbtn_en_1_NextListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            BeanUtil.save2expPanel1(textExpAddr.getText(),
                    textExpUserId.getText(), textExpFile.getText(),
                    textExpLog.getText(), textExpBat.getText(),
                    textExpMaxSize.getText(), textExpStartMonth.getText(),
                    textExpEndMonth.getText(), textExpStartDate.getText(),
                    textExpEndDate.getText(),textExpStartYear.getText(),
                    textExpEndYear.getText());

            getTabExpParamJPanel(2);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tabExpParamPanel.revalidate();
                    tabExpParamPanel.repaint();
                }
            });

        }
    }

    class Jbtn_in_1_NextListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            BeanUtil.save2impPanel1(textImpAddr.getText(),
                    textImpUserId.getText(), textImpLog.getText(),
                    textImpBat.getText());

            getTabImpParamJPanel(2);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tabImpParamPanel.revalidate();
                    tabImpParamPanel.repaint();
                }
            });

        }
    }

    class MaxSizeListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            String maxSize = new BytesChooser().getSize();
            if (!maxSize.endsWith("-")) {
                String str1 = StringUtils.split(maxSize, "-")[1];
                textExpMaxSize.setText(str1);
                Conf4Exp.MAXBYTE = TransferSizeUtil.Str2Long(str1);
                jlMaxSize.setText(TransferSizeUtil.Long2Str(Conf4Exp.MAXBYTE));
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    class Jbtn_en_1_ConnTestListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            BeanUtil.save2expPanel1(textExpAddr.getText(),
                    textExpUserId.getText(), textExpFile.getText(),
                    textExpLog.getText(), textExpBat.getText(),
                    textExpMaxSize.getText(), textExpStartMonth.getText(),
                    textExpEndMonth.getText(), textExpStartDate.getText(),
                    textExpEndDate.getText(),textExpStartYear.getText(),
                    textExpEndYear.getText());
            validateParam(true, 2);
        }
    }

    class Jbtn_in_1_ConnTestListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            BeanUtil.save2impPanel1(textImpAddr.getText(),
                    textImpUserId.getText(), textExpLog.getText(),
                    textExpBat.getText());
            validateParam(false, 2);
        }
    }

    class Jbtn_en_2_BackListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            getTabExpParamJPanel(1);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tabExpParamPanel.revalidate();
                    tabExpParamPanel.repaint();
                }
            });

        }
    }

    class Jbtn_in_2_BackListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            getTabImpParamJPanel(1);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tabImpParamPanel.revalidate();
                    tabImpParamPanel.repaint();
                }
            });

        }
    }

    class Jbtn_en_2_ReSetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // 与直接进入2略微不同，需要重新从配置文件同步一下数据
            Conf4Exp.reReadExp2();

            getTabExpParamJPanel(2);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tabExpParamPanel.revalidate();
                    tabExpParamPanel.repaint();
                }
            });

        }
    }

    class Jbtn_in_2_ReSetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // 与直接进入2略微不同，需要重新从配置文件同步一下数据
            Conf4Imp.reReadImp2();

            getTabImpParamJPanel(2);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tabImpParamPanel.revalidate();
                    tabImpParamPanel.repaint();
                }
            });

        }
    }

    class Jbtn_en_2_FinishListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // 更新参数
            getTabExpParamJPanel(3);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tabExpParamPanel.revalidate();
                    tabExpParamPanel.repaint();
                }
            });
            try {
                connDatabase();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

        }
    }

    class Jbtn_in_2_FinishListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // 更新参数
            getTabImpParamJPanel(3);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tabImpParamPanel.revalidate();
                    tabImpParamPanel.repaint();
                }
            });
            validateParam(false, 1);
        }
    }

    class Jbtn_en_1_SetDefaultListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            BeanUtil.persistent2expPanel1(textExpAddr.getText(),
                    textExpUserId.getText(), textExpFile.getText(),
                    textExpLog.getText(), textExpBat.getText(),
                    textExpMaxSize.getText(), textExpStartMonth.getText(),
                    textExpEndMonth.getText(), textExpStartDate.getText(),
                    textExpEndDate.getText(),textExpStartYear.getText(),
                    textExpEndYear.getText());
        }
    }

    class Jbtn_in_1_SetDefaultListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            BeanUtil.persistent2impPanel1(textImpAddr.getText(),
                    textImpUserId.getText(), textImpLog.getText(),
                    textImpBat.getText());
        }
    }

    class Jbtn_en_1_ReSetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // 先重新从配置文件同步数据，然后重置
            Conf4Exp.reReadExp1();

            textExpAddr.setText(Conf4Exp.ADDR);
            textExpUserId.setText(Conf4Exp.USERID);
            textExpFile.setText(Conf4Exp.FILE);
            textExpLog.setText(Conf4Exp.LOG);
            textExpBat.setText(Conf4Exp.BAT);
            textExpMaxSize.setText(Conf4Exp.MAXSIZE);
            textExpStartMonth.setText(Conf4Exp.STARTMONTH);
            textExpEndMonth.setText(Conf4Exp.ENDMONTH);
            textExpStartDate.setText(Conf4Exp.STARTDATE);
            textExpEndDate.setText(Conf4Exp.ENDDATE);
            textExpStartYear.setText(Conf4Exp.STARTYEAR);
            textExpEndYear.setText(Conf4Exp.ENDYEAR);
        }
    }

    class Jbtn_in_1_ReSetListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // 先重新从配置文件同步数据，然后重置
            Conf4Imp.reReadImp1();

            textImpAddr.setText(Conf4Imp.ADDR);
            textImpUserId.setText(Conf4Imp.USERID);
            textImpLog.setText(Conf4Imp.LOG);
            textImpBat.setText(Conf4Imp.BAT);
        }
    }

    class JmiConnTestListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Conf4Imp.ADDR = textImpAddr.getText();
            Conf4Imp.USERID = textImpUserId.getText();
            // impProperties.setFile(jtfImpPath.getText());
            validateParam(false, 1);
        }
    }

    class JmiAddFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(new File("c:\\"));
            jfc.setFileFilter(new FileFilter() {

                @Override
                public String getDescription() {
                    return "*.dmp";
                }

                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    return f.getName().endsWith(".dmp");
                }
            });
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setMultiSelectionEnabled(true);
            if (jfc.showOpenDialog(DataMigrationTool.this) == JFileChooser.APPROVE_OPTION) {
                // 弹出个对话框,可以选择要上传的文件,如果选择了,就把选择的文件的绝对路径打印出来,
                // 有了绝对路径,通过JTextField的settext就能设置进去了
                // System.out.println(jfc.getSelectedFiles()[0].getName()+jfc.getSelectedFiles()[1].getName());
                Map<String, String> fileMap = Maps.newHashMap();
                for (File f : jfc.getSelectedFiles()) {
                    fileMap.put(f.getPath(), f.getName());
                }
                fillImpTableCell(fileMap);
            } else
                DialogUtil.message("您未选择任何文件", "未选择文件", 2);
        }
    }

    class JmiAddDirListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("C:\\"));
            chooser.setDialogTitle("选择文件夹");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(DataMigrationTool.this) == JFileChooser.APPROVE_OPTION) {
                File f = FileUtils.getFile(chooser.getSelectedFile());
                String[] str = { "dmp" };
                Collection<File> fileList = FileUtils.listFiles(f, str, false);
                Map<String, String> fileMap = Maps.newHashMap();
                for (File file : fileList) {
                    fileMap.put(file.getPath(), file.getName());
                }
                fillImpTableCell(fileMap);
                if (fileMap.size() == 0)
                    DialogUtil.message("该目录下没有可导入文件", "目录扫描结果", 2);
            } else {
                LOGGER.info("No Selection ");
                DialogUtil.message("您未选择任何目录", "目录扫描结果", 2);
            }
        }
    }

    class JmiAboutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DialogUtil.message("Data Migration Tool For Oracle", "关于我们", -1);
        }
    }

    class JmiContactListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DialogUtil.message("Bug反馈邮箱：nuptaxin@gmail.com\n网址：www.renix.org\n内网下载地址：192.1.1.56/resource/dmt-oracle\n源码地址：https://github.com/nuptaxin/dmt-oracle",
                    "联系我们", -1);
        }
    }

    class JtpListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (tabPane.getSelectedIndex() == 0) {
                menuItemConn.setEnabled(true);
                menuItemConnTest.setEnabled(false);
                menuItemAddFile.setEnabled(false);
                menuItemAddDir.setEnabled(false);
            } else {
                menuItemConn.setEnabled(false);
                menuItemConnTest.setEnabled(true);
                menuItemAddFile.setEnabled(true);
                menuItemAddDir.setEnabled(true);

            }
        }
    }

    class JbExpAllListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTableModel jtm = (DefaultTableModel) jt.getModel();
            if (jtm.getRowCount() == 0) {
                DialogUtil.message("没有可以导出的数据，请连接数据库并添加数据", "导出提示", 2);
                return;
            }
            for (int i = 0; i < jtm.getRowCount(); i++) {
                LOGGER.info(jtm.getValueAt(i, 0));
                LOGGER.info(jtm.getValueAt(i, 1));
                ButtonRendererListener.parseRow(
                        jtm.getValueAt(i, 0).toString(), jtm.getValueAt(i, 1)
                                .toString(), jtm.getValueAt(i, 2).toString());
            }

        }
    }

    class JbImpAllListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTableModel jtm = (DefaultTableModel) jtImp.getModel();
            if (jtm.getRowCount() == 0) {
                DialogUtil.message("没有可以导入的数据，请连接数据库并添加数据", "导入提示", 2);
                return;
            }
            for (int i = 0; i < jtm.getRowCount(); i++) {
                LOGGER.info(jtm.getValueAt(i, 0));
                LOGGER.info(jtm.getValueAt(i, 1));
                ButtonRendererListenerImp.parseRow(jtm.getValueAt(i, 1)
                        .toString(), jtm.getValueAt(i, 0).toString());
            }

        }
    }

    class RadioListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton j = (JRadioButton) e.getSource();
            System.out.println(j.getName() + "-" + j.getText());
            for (Field field : f) {
                if (field.getName().equalsIgnoreCase("COMPRESS")) {
                    Conf4Exp.COMPRESS = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("COMPRESS")) {
                    Conf4Exp.COMPRESS = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("CONSISTENT")) {
                    Conf4Exp.CONSISTENT = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("GRANTS")) {
                    Conf4Exp.GRANTS = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("INDEXES")) {
                    Conf4Exp.INDEXES = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("ROWS")) {
                    Conf4Exp.ROWS = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("TRIGGERS")) {
                    Conf4Exp.TRIGGERS = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("CONSTRAINTS")) {
                    Conf4Exp.CONSTRAINTS = "是".equals(j.getText()) ? "Y" : "N";
                }
            }

        }
    }

    class CheckBoxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox j = (JCheckBox) e.getSource();
            System.out.println(j.getName() + "-" + j.getText() + "-"
                    + j.isSelected());
            for (Field field : f) {
                if (field.getName().equalsIgnoreCase("COMPRESS")) {
                    Conf4Exp.COMPRESS = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("COMPRESS")) {
                    Conf4Exp.COMPRESS = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("CONSISTENT")) {
                    Conf4Exp.CONSISTENT = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("GRANTS")) {
                    Conf4Exp.GRANTS = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("INDEXES")) {
                    Conf4Exp.INDEXES = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("ROWS")) {
                    Conf4Exp.ROWS = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("TRIGGERS")) {
                    Conf4Exp.TRIGGERS = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("CONSTRAINTS")) {
                    Conf4Exp.CONSTRAINTS = j.isSelected() ? "Y" : "N";
                }
            }

        }
    }

    class ImpRadioListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton j = (JRadioButton) e.getSource();
            System.out.println(j.getName() + "-" + j.getText());
            for (Field field : fImp) {
                if (field.getName().equalsIgnoreCase("FULL")) {
                    Conf4Imp.FULL = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("GRANTS")) {
                    Conf4Imp.GRANTS = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("INDEXES")) {
                    Conf4Imp.INDEXES = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("ROWS")) {
                    Conf4Imp.ROWS = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("CONSTRAINTS")) {
                    Conf4Imp.CONSTRAINTS = "是".equals(j.getText()) ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("IGNORE")) {
                    Conf4Imp.IGNORE = "是".equals(j.getText()) ? "Y" : "N";
                }
            }

        }
    }

    class CheckBoxImpListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox j = (JCheckBox) e.getSource();
            System.out.println(j.getName() + "-" + j.isSelected());
            for (Field field : fImp) {
                if (field.getName().equalsIgnoreCase("FULL")) {
                    Conf4Imp.FULL = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("GRANTS")) {
                    Conf4Imp.GRANTS = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("INDEXES")) {
                    Conf4Imp.INDEXES = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("ROWS")) {
                    Conf4Imp.ROWS = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("CONSTRAINTS")) {
                    Conf4Imp.CONSTRAINTS = j.isSelected() ? "Y" : "N";
                }
                if (field.getName().equalsIgnoreCase("IGNORE")) {
                    Conf4Imp.IGNORE = j.isSelected() ? "Y" : "N";
                }
            }

        }
    }

    class TextListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                Conf4Exp.OTHER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            try {
                Conf4Exp.OTHER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            try {
                Conf4Exp.OTHER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }

    class TextBufferListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                Conf4Exp.BUFFER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            try {
                Conf4Exp.BUFFER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            try {
                Conf4Exp.BUFFER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }

    class ImpTextListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                Conf4Imp.OTHER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            try {
                Conf4Imp.OTHER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            try {
                Conf4Imp.OTHER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }

    class ImpTextBufferListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                Conf4Imp.BUFFER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            try {
                Conf4Imp.BUFFER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            try {
                Conf4Imp.BUFFER = e.getDocument().getText(
                        e.getDocument().getStartPosition().getOffset(),
                        e.getDocument().getLength());
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException,
            IOException {
        PropertyConfigurator.configure("conf/log4j.properties");
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        DisplayJFrame.runJFrame(new DataMigrationTool(), expToolName, 800, 600);

    }

}
