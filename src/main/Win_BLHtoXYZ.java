package main;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import javabean.BL;
import javabean.BLH;
import javabean.XY;
import javabean.XYZ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

public class Win_BLHtoXYZ {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton xyBLButton;
    private JButton BLXyButton;
    private JButton BLHXYZButton;
    private JButton XYZBLHButton;
    private JTextField textField7;
    private JComboBox comboBox1;
    private JTextField textField8;
    private JTextField textField9;
    private JPanel panel2;

    private double RA, RB, Lo, EPF;
    String[] listData = new String[]{"1954年北京坐标系", "WGS84世界坐标系"};

    public Win_BLHtoXYZ() {
        $$$setupUI$$$();
        BLXyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double Bd, Bf, Bm, Ld, Lf, Lm;
                String[] tmpB, tmpL;
                tmpL = textField1.getText().split(",");
                tmpB = textField2.getText().split(",");
                Ld = Double.valueOf(tmpL[0]);
                Lf = Double.valueOf(tmpL[1]);
                Lm = Double.valueOf(tmpL[2]);

                BL bl = new BL(0, 0);
                bl.l = (Ld + (Lf / 60) + (Lm / 3600));

                Bd = Double.valueOf(tmpB[0]);
                Bf = Double.valueOf(tmpB[1]);
                Bm = Double.valueOf(tmpB[2]);

                bl.b = (Bd + (Bf / 60) + (Bm / 3600));

                Trans trans = new Trans();
                XY xy = new XY(0, 0);
                xy = trans.BLToxy(bl, Lo, RA, RB, xy);
                DecimalFormat df = new DecimalFormat("#.000");
                textField7.setText(df.format(xy.x));
                textField8.setText(df.format(xy.y));
            }
        });
        xyBLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XY xy = new XY(0, 0);
                Trans trans = new Trans();
                BL bl = new BL(0, 0);

                xy.x = Double.valueOf(textField7.getText());
                xy.y = Double.valueOf(textField8.getText());

                trans.xyToBL(xy, Lo, RA, RB, bl);
                textField1.setText(trans.DegToDMS(bl.l));
                textField2.setText(trans.DegToDMS(bl.b));
            }
        });
        BLHXYZButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double N, Bd, Bf, Bm, Ld, Lf, Lm;
                String[] tmpB, tmpL;

                tmpL = textField1.getText().split(",");
                tmpB = textField2.getText().split(",");
                Ld = Double.valueOf(tmpL[0]);
                Lf = Double.valueOf(tmpL[1]);
                Lm = Double.valueOf(tmpL[2]);

                BLH blh = new BLH(0, 0, 0);
                blh.l = (Ld + (Lf / 60) + (Lm / 3600)) * Math.PI / 180;

                Bd = Double.valueOf(tmpB[0]);
                Bf = Double.valueOf(tmpB[1]);
                Bm = Double.valueOf(tmpB[2]);
                blh.b = (Bd + (Bf / 60) + (Bm / 3600)) * Math.PI / 180;

                N = RA / Math.sqrt(1 - EPF * Math.pow(Math.sin(blh.b), 2));
                blh.h = Double.valueOf(textField3.getText());
                XYZ xyz = new XYZ(0, 0, 0);
                xyz.x = (N + blh.h) * Math.cos(blh.b) * Math.cos(blh.l);
                xyz.y = (N + blh.h) * Math.cos(blh.b) * Math.sin(blh.l);
                xyz.z = (N * (1 - EPF) + blh.h) * Math.sin(blh.b);

                DecimalFormat df = new DecimalFormat("#.0000");
                textField4.setText(df.format(xyz.x));
                textField5.setText(df.format(xyz.y));
                textField6.setText(df.format(xyz.z));
            }
        });
        XYZBLHButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double Bt;
                Bt = RA * Math.sqrt(1 - EPF);

                double L, x, y, Z;
                String Lstr, Bstr;

                x = Double.valueOf(textField4.getText());
                y = Double.valueOf(textField5.getText());
                Z = Double.valueOf(textField6.getText());
                L = Math.atan(y / x) * 180 / Math.PI;
                if (L < 0) {
                    L = L + 180;
                }
                Trans trans = new Trans();
                Lstr = trans.DegToDMS(L);
                textField1.setText(Lstr);

                double N0, H0, B0;
                double N1, H1, B1;

                N0 = RA;
                H0 = Math.sqrt(x * x + y * y + Z * Z) - Math.sqrt(RA * Bt);
                double bz1;
                bz1 = Z / Math.sqrt(x * x + y * y);
                B0 = Math.atan(bz1 * (1 - EPF * N0 / (N0 + H0)));
                int i;
                B1 = B0;

                do {
                    B0 = B1;
                    N1 = RA / Math.sqrt(1 - EPF * Math.sin(B0) * Math.sin(B0));
                    H1 = Math.sqrt(x * x + y * y) / Math.cos(B0) - N1;
                    B1 = Math.atan(bz1 / (1 - (EPF * N1) / (N1 + H1)));
                } while (Math.abs(B1 - B0) >= Math.pow(10, -16));
                textField2.setText(trans.DegToDMS(B1 * 180 / Math.PI));
                DecimalFormat df = new DecimalFormat("#.0000");
                textField3.setText(df.format(H1));
            }
        });
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (comboBox1.getSelectedIndex()) {
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
            }
        });

        init();
    }

    private void init() {
        Lo = 117;
        textField9.setText(String.valueOf(Lo));
        textField1.setText("116,35,43.8591");
        textField2.setText("23,25,19.0647");
        textField3.setText("24.493");
        comboBox1.setSelectedIndex(1);
        RA = 6378137;
        RB = RA - RA / 298.257223563;
        EPF = 1 - Math.pow(RB, 2) / Math.pow(RA, 2);
        textField7.setText("2591387.557");
        textField8.setText("458591.946");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Win_BLHtoXYZ");
        frame.setContentPane(new Win_BLHtoXYZ().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1 = new JPanel();
        panel2 = new JPanel();
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
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setLayout(new GridLayoutManager(6, 4, new Insets(10, 10, 10, 10), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-4473925)), null));
        textField1 = new JTextField();
        panel2.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textField2 = new JTextField();
        panel2.add(textField2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textField3 = new JTextField();
        panel2.add(textField3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("经度：");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("纬度：");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("大地高：");
        panel2.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField4 = new JTextField();
        panel2.add(textField4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textField5 = new JTextField();
        panel2.add(textField5, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textField6 = new JTextField();
        panel2.add(textField6, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("X");
        panel2.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Y");
        panel2.add(label5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Z");
        panel2.add(label6, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        xyBLButton = new JButton();
        xyBLButton.setText("xy -> BL");
        panel2.add(xyBLButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BLXyButton = new JButton();
        BLXyButton.setText("BL -> xy");
        panel2.add(BLXyButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BLHXYZButton = new JButton();
        BLHXYZButton.setText("BLH -> XYZ");
        panel2.add(BLHXYZButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        XYZBLHButton = new JButton();
        XYZBLHButton.setText("XYZ -> BLH");
        panel2.add(XYZBLHButton, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("x:");
        panel2.add(label7, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField7 = new JTextField();
        panel2.add(textField7, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("1954年北京坐标系");
        defaultComboBoxModel1.addElement("WGS84世界坐标系");
        comboBox1.setModel(defaultComboBoxModel1);
        panel2.add(comboBox1, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("y:");
        panel2.add(label8, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField8 = new JTextField();
        panel2.add(textField8, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("中央子午线：");
        panel2.add(label9, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_NORTHEAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField9 = new JTextField();
        panel2.add(textField9, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("大地坐标和空间直角坐标系转换");
        panel1.add(label10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}
