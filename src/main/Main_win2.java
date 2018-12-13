package main;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import javabean.ExcelItem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main_win2 {
    private JTable jTable;
    private JList list1 = new JList();
    private JButton FileButton;
    private JButton ExportButton;
    private JTextField FileNameText;
    private JButton FolderButton;
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton EnsureEditButton;
    private JCheckBox AllChooseBox;
    private JScrollPane scrollPane1;
    private JPanel panel2;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private File choosedFile;
    private ArrayList<ExcelItem> itemList;
    private int itemEditIndex = 0;
    private String editItemKey = null;
    private String[] tableHeader = {"选择", "KEY", "X", "Y", "Z", "B", "L", "H"};

    public Main_win2() {


        $$$setupUI$$$();
        FileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });

        /*
         *
         */



        /*
         *导出按钮点击事件
         * 点击之后输出复选框被选择的项
         */
        ExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<ExcelItem> choosedItemList = new ArrayList<ExcelItem>();

                for (int i = 0; i < jTable.getRowCount(); i++) {
                    if ((Boolean) jTable.getModel().getValueAt(i, 0)) {
                        ExcelItem item = new ExcelItem(
                                (String) jTable.getModel().getValueAt(i, 1),
                                (String) jTable.getModel().getValueAt(i, 2),
                                (String) jTable.getModel().getValueAt(i, 3),
                                (String) jTable.getModel().getValueAt(i, 4),
                                (String) jTable.getModel().getValueAt(i, 5),
                                (String) jTable.getModel().getValueAt(i, 6),
                                (String) jTable.getModel().getValueAt(i, 7)
                        );

                        choosedItemList.add(item);


                    }
                }

                for (ExcelItem fuck : choosedItemList) {
                    System.out.println(fuck.toFullString());
                }


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

        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //When double click JList
                    System.out.println("双击了");   //Event
                    //System.out.println(e.getSource());
                    int index = list1.locationToIndex(e.getPoint());
                    itemEditIndex = index;
                    System.out.println(itemEditIndex);
                    ExcelItem itemToEdit = itemList.get(index);
                    editItemKey = itemToEdit.getKey();
                    textField1.setText(itemToEdit.getX());
                    textField2.setText(itemToEdit.getY());
                    textField3.setText(itemToEdit.getZ());
                    textField4.setText(itemToEdit.getB());
                    textField5.setText(itemToEdit.getL());
                    textField6.setText(itemToEdit.getH());

                }
            }
        });

        /*
        确认编辑按钮点击事件
         */
//        EnsureEditButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (editItemKey != null) {
//                    ExcelItem excelItem = itemList.get(itemEditIndex);
//                    excelItem.setKey(editItemKey);
//                    excelItem.setX(textField1.getText());
//                    excelItem.setY(textField2.getText());
//                    excelItem.setZ(textField3.getText());
//                    excelItem.setB(textField4.getText());
//                    excelItem.setL(textField5.getText());
//                    excelItem.setH(textField6.getText());
//                    System.out.println(itemList.get(itemEditIndex));
//
//                    //更新list后重新渲染列表
//                    ListModel listModel; //将对象列表转化为Object数组才能在JList中显示
//                    listModel = new DefaultComboBoxModel(itemList.toArray());
//                    list1.setModel(listModel);
//
//                    //设置List渲染每一项的时候前面带上复选框
////                    MyJcheckBox cell = new MyJcheckBox();
////                    list1.setCellRenderer(cell);
//                    MyPanel myPanel = new MyPanel();
//                    myPanel.init();
//                    list1.setCellRenderer(myPanel);
//
////                    让列表支持多选
//                    list1.setSelectionModel(new DefaultListSelectionModel() {
//                        @Override
//                        public void setSelectionInterval(int index0, int index1) {
//                            if (super.isSelectedIndex(index0)) {
//                                super.removeSelectionInterval(index0, index1);
//                            } else {
//                                super.addSelectionInterval(index0, index1);
//                            }
//                        }
//                    });
//                }
//            }
//        });
    }

    private void readAndShow() {
        try {

            System.out.println("start");

//                    读取Excel文件，得到对象列表
            itemList = ExcelReader.read(choosedFile.getAbsolutePath());
            int tableLength = itemList.size();
            int tableWidth = 8;
            Object[][] rowData = new Object[tableLength][tableWidth];


            for (int i = 0; i < tableLength; i++) {
                ExcelItem tableItem = itemList.get(i);
                rowData[i] = new Object[]{Boolean.FALSE, tableItem.getKey(), tableItem.getX(), tableItem.getY(), tableItem.getZ()
                        , tableItem.getB(), tableItem.getL(), tableItem.getH()};
            }


            MyTableModel myTableModel = new MyTableModel();
            myTableModel.setColumnNames(tableHeader);
            myTableModel.setData(rowData);
            jTable = new JTable();
            jTable.setModel(myTableModel);


            // jTable = new JTable(rowData, tableHeader);
            jTable.getTableHeader().setBackground(new Color(51, 102, 255));
            //jTable.getTableHeader().setForeground(new Color(51, 102, 255));
            jTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 14));


            //解决scrollPanel无法显示jTable的问题，不能用add()方法。
            scrollPane1.setViewportView(jTable);
            System.out.println("end");


//            ListModel listModel; //将对象列表转化为Object数组才能在JList中显示
//            listModel = new DefaultComboBoxModel(itemList.toArray());
//            list1.setModel(listModel);
//
//            //设置List渲染每一项的时候前面带上复选框
////                    MyJcheckBox cell = new MyJcheckBox();
////                    list1.setCellRenderer(cell);
//            MyPanel myPanel = new MyPanel();
//            myPanel.init();
//            list1.setCellRenderer(myPanel);
//
////                    让列表支持多选
//            jTable.setSelectionModel(new DefaultListSelectionModel() {
//                @Override
//                public void setSelectionInterval(int index0, int index1) {
//                    if (super.isSelectedIndex(index0)) {
//                        super.removeSelectionInterval(index0, index1);
//                    } else {
//                        super.addSelectionInterval(index0, index1);
//                    }
//                }
//            });


        } catch (Exception ee) {
            ee.printStackTrace();
        }


    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1 = new JPanel();
        // TODO: place custom component creation code here
        scrollPane1 = new JScrollPane();
        panel2 = new JPanel();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("mainWindow");
        Main_win2 win = new Main_win2();
        frame.setContentPane(win.panel1);
        frame.setBounds(350, 250, 500, 100);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
        panel1.setLayout(new GridLayoutManager(3, 9, new Insets(0, 0, 0, 0), -1, -1));
        ExportButton = new JButton();
        ExportButton.setText("导出");
        panel1.add(ExportButton, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(90, -1), new Dimension(90, -1), new Dimension(90, -1), 0, false));
        FileButton = new JButton();
        FileButton.setMargin(new Insets(0, 0, 0, 0));
        FileButton.setText("文件");
        panel1.add(FileButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(90, -1), new Dimension(90, -1), new Dimension(90, -1), 1, false));
        panel1.add(scrollPane1, new GridConstraints(2, 0, 1, 9, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(800, 500), new Dimension(800, 500), new Dimension(800, 500), 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("             ");
        panel1.add(label1, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("        ");
        panel1.add(label2, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        FileNameText = new JTextField();
        FileNameText.setEditable(false);
        panel1.add(FileNameText, new GridConstraints(0, 2, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("选择什么");
        panel1.add(label3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox1 = new JComboBox();
        panel1.add(comboBox1, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("选择什么");
        panel1.add(label4, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox2 = new JComboBox();
        panel1.add(comboBox2, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        FolderButton = new JButton();
        FolderButton.setText("文件夹");
        panel1.add(FolderButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(90, -1), new Dimension(90, -1), new Dimension(90, -1), 1, false));
        AllChooseBox = new JCheckBox();
        AllChooseBox.setText("全选");
        panel1.add(AllChooseBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}

