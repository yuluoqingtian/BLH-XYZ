package main;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.sun.org.apache.xpath.internal.SourceTree;
import javabean.ExcelItem;
import javabean.XYZ;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class Main_win2 {
    private JTable jTable = null;
    private JTextField FileNameText;
    JPanel panel1;
    private JCheckBox AllChooseBox;
    private JScrollPane scrollPane1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton changeButton;
    private JButton addButton;
    private File choosedFile;
    private ArrayList<ExcelItem> itemList;
    private String[] tableHeader = {"选择", "KEY", "X", "Y", "Z", "B", "L", "H"};
    JMenuBar jMenuBar = new JMenuBar();
    /*
     * 创建 "文件" 一级菜单的子菜单
     */
    private JMenuItem newMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem exportMenuItem;
    private JMenuItem exitMenuItem;
    private MyTableModel myTableModel = null;
    //private ArrayList<ExcelItem> chooseItemList = new ArrayList<ExcelItem>();
    private LinkedHashMap<Integer, ExcelItem> chooseItemMap = new LinkedHashMap<Integer, ExcelItem>();

    /*
     * 创建一级菜单
     */
    private JMenu fileMenu = new JMenu("文件");
    private JMenu aboutMenu = new JMenu("关于");
    private double RA, RB, Lo, EPF;


    private void OpenFile() {
        try {
            //设置界面风格
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //设置文件选择窗口开始打开的路径，目前设置为项目所在目录
            JFileChooser jfc = new JFileChooser(new File("./"));

            //设置文件名过滤器，只能显示xls，xlsx两种文件
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Excel表格文件: xls,xlsx", "xls", "xlsx");
            jfc.setFileFilter(filter);

            //设置选择路径模式
            jfc.setDialogTitle("请选择路径");
            if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(null)) {
                //用户点击了确定
                String path = jfc.getSelectedFile().getAbsolutePath();
                choosedFile = new File(path);
                FileNameText.setText(choosedFile.getAbsolutePath());
                //System.out.println(path);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private void Export(String initPath) {
        try {

            if (jTable.getModel().getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "表格没有数据不能导出", " 错误", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //设置界面风格
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //设置文件选择窗口开始打开的路径，目前设置为项目所在目录
            JFileChooser chooser = new JFileChooser(new File(initPath));
            //把按钮文字改成保存
            chooser.setApproveButtonText("保存");


            //后缀名过滤器
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Excel表格文件(*.xlsx)", "xlsx"
            );
            chooser.setFileFilter(filter);

            //下面的方法将阻塞，直到用户按下保存按钮且文件名文本框不为空或者用户按下取消按钮
            int option = chooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {//用户选择了保存
                File file = chooser.getSelectedFile();
                String fileName = chooser.getName(file); //从文件名输入框中获取文件名

                //如果用户填写的文件名不带后缀，则自动添加
                if (fileName.indexOf(".xls") == -1) {
                    file = new File(chooser.getCurrentDirectory(), fileName + ".xlsx");
                }


                //如果文件存在，则弹出错误提示框，并且在原来用户想保存的地方再次打开保存窗口
                if (file.exists()) {
                    System.out.println("文件已存在");
                    JOptionPane.showMessageDialog(null, "文件已存在，请重新命名", " 错误", JOptionPane.ERROR_MESSAGE);
                    //Export(file.getParent());
                    return;
                }

                //写文件
                ExcelWriter.write(file.getAbsoluteFile().toString(), myTableModel);

                //写完之后检查一下成功没有
                if (file.exists()) {
                    JOptionPane.showMessageDialog(null, "保存成功", " 成功", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "保存失败", " 错误", JOptionPane.WARNING_MESSAGE);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return chooseCount:用户一共勾选了几项，为0说明用户没有勾选
     * 此方法执行后,将用户选择的items赋值给ArrayList<ExcelItem> chooseItemList
     */
    private int computedChoose() {
        int chooseCount = 0;
        chooseItemMap.clear();
        for (int i = 0; i < jTable.getRowCount(); i++) {
            if ((Boolean) jTable.getModel().getValueAt(i, 0)) {
                ExcelItem item = new ExcelItem(
                        (String) jTable.getModel().getValueAt(i, 2),
                        (String) jTable.getModel().getValueAt(i, 3),
                        (String) jTable.getModel().getValueAt(i, 4),
                        (String) jTable.getModel().getValueAt(i, 5),
                        (String) jTable.getModel().getValueAt(i, 6),
                        (String) jTable.getModel().getValueAt(i, 7)
                );
                chooseCount++;
                chooseItemMap.put(i + 1, item);
            }
        }
        return chooseCount;
    }


    Main_win2() {


        $$$setupUI$$$();

        /*
         * 菜单项的点击/状态改变事件监听，事件监听可以直接设置在具体的子菜单上（这里只设置其中几个）
         */
        // 设置 "新建" 子菜单被点击的监听器
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenFile();
            }
        });
        // 设置 "打开" 子菜单被点击的监听器
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("打开  被点击");
            }
        });

        exportMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Export("./");
            }
        });
        // 设置 "退出" 子菜单被点击的监听器
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        AllChooseBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //全选复选框的监听事件

//                如果全选框被选中就把所有没选中的选中
                if (AllChooseBox.isSelected()) {

                    MyTableModel myTableModel = (MyTableModel) jTable.getModel();
                    myTableModel.selectAllOrNull(Boolean.TRUE);
                    jTable.setModel(myTableModel);
//                    如果全选框没选中就把选中的都取消
                } else {
                    MyTableModel myTableModel = (MyTableModel) jTable.getModel();
                    myTableModel.selectAllOrNull(Boolean.FALSE);
                    jTable.setModel(myTableModel);
                }
            }
        });

        //第一个下拉框改变时的监听事件
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    //System.out.println(comboBox1.getSelectedItem());
                    if (comboBox1.getSelectedItem().equals("BLH -> XYZ") || comboBox1.getSelectedItem().equals("XYZ -> BLH")) {
                        //重新设置表头
                        myTableModel.setColumnNames(new String[]{"选择", "KEY", "X", "Y", "Z", "B", "L", "H"});
                        //通知表头重新渲染
                        myTableModel.fireTableStructureChanged();
                    } else if (comboBox1.getSelectedItem().equals("BL -> XY") || comboBox1.getSelectedItem().equals("XY -> BL")) {
                        myTableModel.setColumnNames(new String[]{"选择", "KEY", "X", "Y", "", "B", "L", ""});
                        myTableModel.fireTableStructureChanged();
                    } else if (comboBox1.getSelectedItem().equals("四维")) {
                        myTableModel.setColumnNames(new String[]{"选择", "KEY", "X", "Y", "", "X1", "Y1", ""});
                        myTableModel.fireTableStructureChanged();
                    } else if (comboBox1.getSelectedItem().equals("七维")) {
                        myTableModel.setColumnNames(new String[]{"选择", "KEY", "X", "Y", "Z", "X1", "Y1", "Z1"});
                        myTableModel.fireTableStructureChanged();
                    }
                }
            }
        });

        //转换按钮的监听事件
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                1、首先看看用户选择了什么，如果什么都没选，提示他勾选
                2、如果他选了，看看他下拉框选了是什么东西
                 */

                //统计用户选择的数据
                int count = computedChoose();

                //表格没数据
                if (jTable.getModel().getRowCount() == 0) {
                    JOptionPane.showMessageDialog(scrollPane1, "表格数据为空", " 错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //一行也没选
                if (count == 0) {
                    JOptionPane.showMessageDialog(scrollPane1, "请先选择若干行，才能进行转换", " 错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                switch (comboBox2.getSelectedIndex()) {
                    case 0:
                        RA = 6378245;
                        RB = RA - RA / 298.3;
                        EPF = 1 - Math.pow(RB, 2) / Math.pow(RA, 2);
                        break;
                    case 1:
                        RA = 6378137;
                        RB = RA - RA / 298.257223;
                        EPF = 1 - Math.pow(RB, 2) / Math.pow(RA, 2);
                        break;
                    default:

                        break;
                }


                //如果用户选了几行，看看他下拉框里面选的是什么
                if (comboBox1.getSelectedItem().toString().equals("XYZ -> BLH")) {
                    //将符合条件的坐标进行转换

                    //成功转换的数量
                    int succcess = 0;
                    //System.out.println("\n通过Map.entrySet遍历key和value");
                    //遍历LinkedHashMap,key就是行数
                    for (HashMap.Entry<Integer, ExcelItem> entry : chooseItemMap.entrySet()) {
                        //System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue().toFullString());
                        if (entry.getValue().getX() != null && !(entry.getValue().getX().equals(""))
                                && entry.getValue().getY() != null && !(entry.getValue().getY().equals(""))
                                && entry.getValue().getZ() != null && !(entry.getValue().getZ().equals(""))) {
                            String[] convetData = XYZtoBLH(entry.getValue().getX(), entry.getValue().getY(), entry.getValue().getZ());

                            myTableModel.setValueAt(convetData[0], entry.getKey() - 1, 5);
                            myTableModel.setValueAt(convetData[1], entry.getKey() - 1, 6);
                            myTableModel.setValueAt(convetData[2], entry.getKey() - 1, 7);
                            succcess++;
                        }
                    }
                    //System.out.println("共选择" + count + "项，" + "成功转换" + succcess + "项");
                    JOptionPane.showMessageDialog(null, "共选择 " + count + " 项，" + "成功转换符合条件数据 " + succcess + " 项", "XYZ -> BLH", JOptionPane.INFORMATION_MESSAGE);

                } else if (comboBox1.getSelectedItem().toString().equals("BLH -> XYZ")) {
                    int succcess = 0;
                    //System.out.println("\n通过Map.entrySet遍历key和value");
                    //遍历LinkedHashMap,key就是行数
                    for (HashMap.Entry<Integer, ExcelItem> entry : chooseItemMap.entrySet()) {
                        //System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue().toFullString());
                        if (entry.getValue().getB() != null && !(entry.getValue().getB().equals(""))
                                && entry.getValue().getL() != null && !(entry.getValue().getL().equals(""))
                                && entry.getValue().getH() != null && !(entry.getValue().getH().equals(""))) {
                            String[] convetData = BLHtoXYZ(entry.getValue().getB(), entry.getValue().getL(), entry.getValue().getH());

                            myTableModel.setValueAt(convetData[0], entry.getKey() - 1, 2);
                            myTableModel.setValueAt(convetData[1], entry.getKey() - 1, 3);
                            myTableModel.setValueAt(convetData[2], entry.getKey() - 1, 4);
                            succcess++;
                        }
                    }
                    //System.out.println("共选择" + count + "项，" + "成功转换" + succcess + "项");
                    JOptionPane.showMessageDialog(null, "共选择 " + count + " 项，" + "成功转换符合条件数据 " + succcess + " 项", "BLH -> XYZ", JOptionPane.INFORMATION_MESSAGE);

                } else if (comboBox1.getSelectedItem().toString().equals("XY -> BL")) {
                    int succcess = 0;
                    for (HashMap.Entry<Integer, ExcelItem> entry : chooseItemMap.entrySet()) {
                        if (entry.getValue().getX() != null && !(entry.getValue().getX().equals(""))
                                && entry.getValue().getY() != null && !(entry.getValue().getY().equals(""))) {

                            String[] convetData = XYtoBL(entry.getValue().getX(), entry.getValue().getY());
                            myTableModel.setValueAt(convetData[0], entry.getKey() - 1, 5);
                            myTableModel.setValueAt(convetData[1], entry.getKey() - 1, 6);
                            succcess++;
                        }
                    }
                    JOptionPane.showMessageDialog(null, "共选择 " + count + " 项，" + "成功转换符合条件数据 " + succcess + " 项", "XY -> BL", JOptionPane.INFORMATION_MESSAGE);
                } else if (comboBox1.getSelectedItem().toString().equals("BL -> XY")) {
                    int succcess = 0;
                    for (HashMap.Entry<Integer, ExcelItem> entry : chooseItemMap.entrySet()) {
                        if (entry.getValue().getB() != null && !(entry.getValue().getB().equals(""))
                                && entry.getValue().getL() != null && !(entry.getValue().getL().equals(""))) {

                            String[] convetData = BLtoXY(entry.getValue().getB(), entry.getValue().getL());
                            myTableModel.setValueAt(convetData[0], entry.getKey() - 1, 2);
                            myTableModel.setValueAt(convetData[1], entry.getKey() - 1, 3);
                            succcess++;
                        }
                    }
                    JOptionPane.showMessageDialog(null, "共选择 " + count + " 项，" + "成功转换符合条件数据 " + succcess + " 项", "BL -> XY", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });


        FileNameText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                //一旦文件选择框的内容发生改变
                //就直接读取文件筐中的EXCEL文件并显示
                readAndShow();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                readAndShow();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                readAndShow();
            }
        });


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("点击了+");
                if (jTable != null) {
                    //知道原来有多少条数据
                    int oldDataLength = jTable.getModel().getRowCount();
                    //System.out.println(oldDataLength);

                    //创建一个比原来数组多一行的新数组
                    Object[][] newRowData = new Object[oldDataLength + 1][8];

                    //把旧数组赋值给新的，除了最后一行
                    for (int i = 0; i < oldDataLength; i++) {
                        newRowData[i] = myTableModel.getRowValueByRowIndex(i);
                    }


                    //创建最后一行的数据
                    newRowData[oldDataLength][0] = Boolean.FALSE;
                    newRowData[oldDataLength][1] = String.valueOf(oldDataLength + 1);
                    for (int i = 2; i < 8; i++) {
                        newRowData[oldDataLength][i] = "";
                    }

                    myTableModel.setData(newRowData);
                    jTable.setModel(myTableModel);
                    scrollPane1.setViewportView(jTable);

                    //焦点移到最后一行
                    jTable.setRowSelectionInterval(jTable.getModel().getRowCount() - 1, jTable.getModel().getRowCount() - 1);
                    scrollPane1.getViewport().setViewPosition(new Point(0, scrollPane1.getHeight()));


                } else {
                    //若表格未创建，弹出错误提示框
                    JOptionPane.showMessageDialog(null, "表格未创建", " 错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void readAndShow() {
        try {
//                    读取Excel文件，得到对象列表
            itemList = ExcelReader.read(choosedFile.getAbsolutePath());
            int tableLength = itemList.size();
            int tableWidth = 8;
            Object[][] rowData = new Object[tableLength][tableWidth];


            for (int i = 0; i < tableLength; i++) {
                ExcelItem tableItem = itemList.get(i);
                rowData[i] = new Object[]{Boolean.FALSE, String.valueOf(i + 1), tableItem.getX(), tableItem.getY(), tableItem.getZ()
                        , tableItem.getB(), tableItem.getL(), tableItem.getH()};
            }
            myTableModel.setColumnNames(tableHeader);
            myTableModel.setData(rowData);
            jTable.setModel(myTableModel);


            //设置表头的字体和背景颜色
            //jTable.getTableHeader().setBackground(Color.white);
            //jTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 14));


            //解决scrollPanel无法显示jTable的问题，不能用add()方法。
            scrollPane1.setViewportView(jTable);
            System.out.println("end");
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    /**
     * @param X
     * @param Y
     * @param Z
     * @param coordinate 坐标系类型，值为"1954年北京坐标系"或者"WGS84世界坐标系"
     * @return 返回BLH的值，String[0],String[1],String[2],下面的方法类似
     */
    private String[] XYZtoBLH(String X, String Y, String Z) {
        //todo String[0]=B,String[1]=L,String=H ,注意针对不同坐标系
        String[] result = new String[3];
        result[0] = X;
        result[1] = Y;
        result[2] = Z;
        return result;
    }

    private String[] BLHtoXYZ(String B, String L, String H) {
        String[] result = new String[3];
        result[0] = B;
        result[1] = L;
        result[2] = H;
        return result;
    }

    private String[] BLtoXY(String B, String L) {
        String[] result = new String[2];
        result[0] = B;
        result[1] = L;
        return result;
    }

    private String[] XYtoBL(String X, String Y) {
        String[] result = new String[2];
        result[0] = X;
        result[1] = Y;
        return result;
    }


    /**
     * 创建组件的步骤在这里写
     */
    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1 = new JPanel();
        // TODO: place custom component creation code here
        scrollPane1 = new JScrollPane();

        //创建三个菜单项
        newMenuItem = new JMenuItem("打开文件");
        openMenuItem = new JMenuItem("打开文件夹");
        exportMenuItem = new JMenuItem("导出");
        exitMenuItem = new JMenuItem("退出");

        //把菜单项加到菜单上
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(exportMenuItem);
        fileMenu.addSeparator();       // 添加一条分割线
        fileMenu.add(exitMenuItem);

        //把菜单加到菜单条上
        jMenuBar.add(fileMenu);
        jMenuBar.add(aboutMenu);


        myTableModel = new MyTableModel();
        Object[][] rowData = new Object[0][8];


        myTableModel.setColumnNames(tableHeader);
        myTableModel.setData(rowData);

        jTable = new JTable();
        jTable.setModel(myTableModel);


        //设置表头的字体和背景颜色
        //jTable.getTableHeader().setBackground(Color.white);
        //jTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 14));
        //解决scrollPanel无法显示jTable的问题，不能用add()方法。


        scrollPane1.setViewportView(jTable);

    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel1.setLayout(new GridLayoutManager(4, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(scrollPane1, new GridConstraints(2, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(1500, 800), new Dimension(1500, 800), new Dimension(1500, 800), 0, false));
        FileNameText = new JTextField();
        FileNameText.setEditable(false);
        panel1.add(FileNameText, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("操作");
        panel1.add(label1, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("坐标系");
        panel1.add(label2, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox2 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("1954年北京坐标系");
        defaultComboBoxModel1.addElement("WGS84世界坐标系");
        comboBox2.setModel(defaultComboBoxModel1);
        panel1.add(comboBox2, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        AllChooseBox = new JCheckBox();
        AllChooseBox.setText("全选");
        panel1.add(AllChooseBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        changeButton = new JButton();
        changeButton.setText("转换");
        panel1.add(changeButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addButton = new JButton();
        addButton.setText("+");
        panel1.add(addButton, new GridConstraints(3, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("XYZ -> BLH");
        defaultComboBoxModel2.addElement("BLH -> XYZ");
        defaultComboBoxModel2.addElement("XY -> BL");
        defaultComboBoxModel2.addElement("BL -> XY");
        defaultComboBoxModel2.addElement("四维");
        defaultComboBoxModel2.addElement("七维");
        comboBox1.setModel(defaultComboBoxModel2);
        panel1.add(comboBox1, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}

