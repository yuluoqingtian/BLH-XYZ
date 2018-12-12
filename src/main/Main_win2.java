package main;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import javabean.ExcelItem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
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
    private JComboBox OperateChooseBox;
    private JPanel panel1;
    private JButton TypeChooseButton;
    private JButton OpreateChooseButton;
    private JComboBox TypeChooseBox;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton EnsureEditButton;
    private JLabel EditLabel;
    private JCheckBox AllChooseBox;
    private JScrollPane scrollPane1;
    private File choosedFile;
    private ArrayList<ExcelItem> itemList;
    private int itemEditIndex = 0;
    private String editItemKey = null;


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
         */
        ExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ExcelItem> chooesdExcelItemList = new ArrayList<ExcelItem>();  //被选择的对象组成的列表
                //确认选择按钮的监听事件
                Object[] value = list1.getSelectedValues();

                for (Object i : value) {
                    ExcelItem item = (ExcelItem) i; //强制类型转换把object转换回来
                    chooesdExcelItemList.add(item);
                }

                for (ExcelItem item : chooesdExcelItemList) {
                    System.out.println(item.getKey());
                }
            }
        });


        AllChooseBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //全选复选框的监听事件

//                如果全选框被选中就把所有没选中的选中
                if (AllChooseBox.isSelected()) {
                    System.out.println("被选中");
                    for (int i = 0; i < list1.getModel().getSize(); i++) {
                        if (!list1.isSelectedIndex(i)) {
                            list1.setSelectedIndex(i);
                        }
                    }
//                    如果全选框没选中就把选中的都取消
                } else {
                    for (int i = 0; i < list1.getModel().getSize(); i++) {
                        if (list1.isSelectedIndex(i)) {
                            list1.setSelectedIndex(i);
                        }
                    }
                    System.out.println("没被选中");
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

//        list1.addMouseListener(new MouseAdapter() {
//
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
////    maybeShowPopup(e);
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                list1.setSelectedIndex(list1.locationToIndex(e.getPoint())); //获取鼠标点击的项
//                maybeShowPopup(e);
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                maybeShowPopup(e);
//            }
//
//            //弹出菜单
//            private void maybeShowPopup(MouseEvent e) {
//                if (e.isPopupTrigger() && list1.getSelectedIndex() != -1) {
//
//                    //获取选择项的值
//                    Object selected = list1.getModel().getElementAt(list1.getSelectedIndex());
//                    System.out.println(selected);
//                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
//                }
//            }
//
//
//        });

        /*
        确认编辑按钮点击事件
         */
        EnsureEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (editItemKey != null) {
                    ExcelItem excelItem = itemList.get(itemEditIndex);
                    excelItem.setKey(editItemKey);
                    excelItem.setX(textField1.getText());
                    excelItem.setY(textField2.getText());
                    excelItem.setZ(textField3.getText());
                    excelItem.setB(textField4.getText());
                    excelItem.setL(textField5.getText());
                    excelItem.setH(textField6.getText());
                    System.out.println(itemList.get(itemEditIndex));

                    //更新list后重新渲染列表
                    ListModel listModel; //将对象列表转化为Object数组才能在JList中显示
                    listModel = new DefaultComboBoxModel(itemList.toArray());
                    list1.setModel(listModel);

                    //设置List渲染每一项的时候前面带上复选框
//                    MyJcheckBox cell = new MyJcheckBox();
//                    list1.setCellRenderer(cell);
                    MyPanel myPanel = new MyPanel();
                    myPanel.init();
                    list1.setCellRenderer(myPanel);

//                    让列表支持多选
                    list1.setSelectionModel(new DefaultListSelectionModel() {
                        @Override
                        public void setSelectionInterval(int index0, int index1) {
                            if (super.isSelectedIndex(index0)) {
                                super.removeSelectionInterval(index0, index1);
                            } else {
                                super.addSelectionInterval(index0, index1);
                            }
                        }
                    });
                }
            }
        });
    }

    private void readAndShow() {
        try {

//                    读取Excel文件，得到对象列表
            itemList = ExcelReader.read(choosedFile.getAbsolutePath());
            ListModel listModel; //将对象列表转化为Object数组才能在JList中显示
            listModel = new DefaultComboBoxModel(itemList.toArray());
            list1.setModel(listModel);

            //设置List渲染每一项的时候前面带上复选框
//                    MyJcheckBox cell = new MyJcheckBox();
//                    list1.setCellRenderer(cell);
            MyPanel myPanel = new MyPanel();
            myPanel.init();
            list1.setCellRenderer(myPanel);

//                    让列表支持多选
            list1.setSelectionModel(new DefaultListSelectionModel() {
                @Override
                public void setSelectionInterval(int index0, int index1) {
                    if (super.isSelectedIndex(index0)) {
                        super.removeSelectionInterval(index0, index1);
                    } else {
                        super.addSelectionInterval(index0, index1);
                    }
                }
            });


        } catch (Exception ee) {
            ee.printStackTrace();
        }


    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1 = new JPanel();
        // TODO: place custom component creation code here
        scrollPane1 = new JScrollPane(list1);
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
        panel1.setLayout(new GridLayoutManager(5, 11, new Insets(0, 0, 0, 0), -1, -1));
        FileNameText = new JTextField();
        FileNameText.setEditable(false);
        panel1.add(FileNameText, new GridConstraints(0, 2, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        ExportButton = new JButton();
        ExportButton.setText("导出");
        panel1.add(ExportButton, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        FileButton = new JButton();
        FileButton.setMargin(new Insets(0, 0, 0, 0));
        FileButton.setText("文件");
        panel1.add(FileButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        FolderButton = new JButton();
        FolderButton.setText("文件夹");
        panel1.add(FolderButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        OperateChooseBox = new JComboBox();
        OperateChooseBox.setToolTipText("1,2,3，4,5");
        panel1.add(OperateChooseBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(80, -1), new Dimension(80, -1), new Dimension(80, -1), 0, false));
        TypeChooseButton = new JButton();
        TypeChooseButton.setText("选择类型");
        panel1.add(TypeChooseButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        TypeChooseBox = new JComboBox();
        panel1.add(TypeChooseBox, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(75, -1), new Dimension(75, -1), new Dimension(100, -1), 0, false));
        textField1 = new JTextField();
        panel1.add(textField1, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), new Dimension(50, -1), new Dimension(50, -1), 0, false));
        textField2 = new JTextField();
        textField2.setText("");
        panel1.add(textField2, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), new Dimension(50, -1), new Dimension(50, -1), 0, false));
        textField3 = new JTextField();
        panel1.add(textField3, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), new Dimension(50, -1), new Dimension(50, -1), 0, false));
        textField4 = new JTextField();
        panel1.add(textField4, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), new Dimension(50, -1), new Dimension(50, -1), 0, false));
        textField5 = new JTextField();
        panel1.add(textField5, new GridConstraints(2, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), new Dimension(50, -1), new Dimension(50, -1), 0, false));
        textField6 = new JTextField();
        panel1.add(textField6, new GridConstraints(2, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(50, -1), new Dimension(50, -1), new Dimension(50, -1), 0, false));
        EnsureEditButton = new JButton();
        EnsureEditButton.setText("确定");
        panel1.add(EnsureEditButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        EditLabel = new JLabel();
        EditLabel.setText("编辑");
        panel1.add(EditLabel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        OpreateChooseButton = new JButton();
        OpreateChooseButton.setText("选择操作");
        panel1.add(OpreateChooseButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        AllChooseBox = new JCheckBox();
        AllChooseBox.setText("全选");
        panel1.add(AllChooseBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("key");
        panel1.add(label1, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("X       ");
        panel1.add(label2, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Y");
        panel1.add(label3, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Z");
        panel1.add(label4, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("B");
        panel1.add(label5, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("L");
        panel1.add(label6, new GridConstraints(3, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("H");
        panel1.add(label7, new GridConstraints(3, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(scrollPane1, new GridConstraints(4, 1, 1, 9, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(800, 500), new Dimension(800, 500), new Dimension(800, 500), 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("             ");
        panel1.add(label8, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("        ");
        panel1.add(label9, new GridConstraints(0, 10, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}

