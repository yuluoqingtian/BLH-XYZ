package main;

import Component.MyJcheckBox;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import javabean.ExcelItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main_win {
    private JPanel panel1;
    private JList list1 = new JList();
    private JTextField textField1;
    private JButton choose;
    private JCheckBox allCheckBox;
    private JPanel panel2;
    private JButton XYBLButton;
    private JButton BLXYButton;
    private JButton XYZBLHButton;
    private JButton BLHXYZButton;
    private JButton esure;
    private JScrollPane scrollPane1;
    private JButton ensureChoose;
    private JPanel panel3;
    private File choosedFile;
    private List itemList;

    private JMenuBar jMenuBar;
    private JMenu jm1, jm2;
    private JMenuItem jmi11, jmi12, jmi13;

    public Main_win() {
        //选择按钮的监听事件
        $$$setupUI$$$();
        choose.addActionListener(new ActionListener() {
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
                        textField1.setText(choosedFile.getAbsolutePath());
                        //System.out.println(path);
                    }

                } catch (Exception error) {
                    error.printStackTrace();
                }

            }
        });

        //确定按钮的监听事件
        esure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
        });
        ensureChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<ExcelItem> chooesdExcelItemList = new ArrayList<ExcelItem>();  //被选择的对象组成的列表
                //确认选择按钮的监听事件
                Object[] value = list1.getSelectedValues();

                for (Object i : value) {
                    ExcelItem item = (ExcelItem) i; //强制类型转换把object转换回来
                    chooesdExcelItemList.add(item);
                }

                System.out.println(chooesdExcelItemList.toString());

            }
        });

        allCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //全选复选框的监听事件

//                如果全选框被选中就把所有没选中的选中
                if (allCheckBox.isSelected()) {
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


    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1 = new JPanel();
        panel2 = new JPanel();
        scrollPane1 = new JScrollPane(list1);
        panel3 = new JPanel();

        jMenuBar = new JMenuBar();
        jm1 = new JMenu("文件");
        jm2 = new JMenu("帮助");
        jmi11 = new JMenuItem("打开文件");
        jmi12 = new JMenuItem("导出文件");
        jmi13 = new JMenuItem("打开文件夹");
        jm1.add(jmi11);
        jm1.add(jmi12);
        jm1.add(jmi13);
        jMenuBar.add(jm1);
        jMenuBar.add(jm2);


    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("mainWindow");
        Main_win win = new Main_win();
        frame.setContentPane(win.panel1);
        frame.setBounds(350, 250, 500, 100);
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
        panel1.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("当前文件夹");
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        textField1.setEditable(false);
        panel1.add(textField1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        choose = new JButton();
        choose.setMargin(new Insets(3, 18, 3, 18));
        choose.setText("选择文件夹");
        panel1.add(choose, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        esure = new JButton();
        esure.setActionCommand("确定");
        esure.setLabel("确定");
        esure.setText("确定");
        panel1.add(esure, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(scrollPane1, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(500, 500), new Dimension(1000, 500), new Dimension(2000, 2000), 0, false));
        panel3.setLayout(new GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("key");
        panel3.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("X");
        panel3.add(label3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Y");
        panel3.add(label4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Z");
        panel3.add(label5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("B");
        panel3.add(label6, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("L");
        panel3.add(label7, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("H");
        panel3.add(label8, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("");
        panel3.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        allCheckBox = new JCheckBox();
        allCheckBox.setText("全选");
        panel1.add(allCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:d:noGrow"));
        panel1.add(panel2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        BLXYButton = new JButton();
        BLXYButton.setText("BL -> XY");
        CellConstraints cc = new CellConstraints();
        panel2.add(BLXYButton, cc.xy(3, 1));
        XYZBLHButton = new JButton();
        XYZBLHButton.setText("XYZ -> BLH");
        panel2.add(XYZBLHButton, cc.xy(5, 1));
        BLHXYZButton = new JButton();
        BLHXYZButton.setText("BLH -> XYZ");
        panel2.add(BLHXYZButton, cc.xy(7, 1));
        XYBLButton = new JButton();
        XYBLButton.setText("XY -> BL");
        panel2.add(XYBLButton, cc.xy(1, 1));
        ensureChoose = new JButton();
        ensureChoose.setActionCommand("确认选择");
        ensureChoose.setText("确认选择");
        panel1.add(ensureChoose, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JToolBar toolBar1 = new JToolBar();
        panel1.add(toolBar1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
