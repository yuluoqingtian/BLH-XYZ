package main;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import javabean.ExcelItem;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;


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
    private ArrayList<ExcelItem> chooseItemList = new ArrayList<ExcelItem>();

    /*
     * 创建一级菜单
     */
    private JMenu fileMenu = new JMenu("文件");
    private JMenu aboutMenu = new JMenu("关于");


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

    private void Export() {
        computedChoose();
        for (ExcelItem fuck : chooseItemList) {
            System.out.println(fuck.toFullString());
        }
    }

    /**
     * @return chooseCount:用户一共勾选了几项，为0说明用户没有勾选
     * 此方法执行后,将用户选择的items赋值给ArrayList<ExcelItem> chooseItemList
     */
    private int computedChoose() {
        int chooseCount = 0;
        chooseItemList.clear();
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
                chooseItemList.add(item);
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
                Export();
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


        //转换按钮的监听事件
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                1、首先看看用户选择了什么，如果什么都没选，提示他勾选
                2、如果他选了，看看他下拉框选了是什么东西
                 */

                int count = computedChoose();

                //表格没数据
                if (jTable.getModel().getRowCount() == 0) {
                    JOptionPane.showMessageDialog(scrollPane1, "表格数据为空", " 错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //一行也没选
                if (count == 0) {
                    JOptionPane.showMessageDialog(scrollPane1, "请先选择若干行，才能进行转换", " 错误", JOptionPane.ERROR_MESSAGE);
                    //return;
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

