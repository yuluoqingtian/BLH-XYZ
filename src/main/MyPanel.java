package main;

import Component.MyJcheckBox;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MyPanel extends JPanel implements ListCellRenderer {

    private MyJcheckBox checkBox = new MyJcheckBox();
    private JLabel label1 = new JLabel();
    private JLabel label2 = new JLabel();
    private JLabel label3 = new JLabel();
    private JLabel label4 = new JLabel();
    private JLabel label5 = new JLabel();
    private JLabel label6 = new JLabel();
    private JLabel label7 = new JLabel();

    public void init(){
        this.setLayout(new GridLayout(1,8));
        label1.setHorizontalAlignment(JLabel.CENTER);
        label2.setHorizontalAlignment(JLabel.CENTER);
        label3.setHorizontalAlignment(JLabel.CENTER);
        label4.setHorizontalAlignment(JLabel.CENTER);
        label5.setHorizontalAlignment(JLabel.CENTER);
        label6.setHorizontalAlignment(JLabel.CENTER);
        label7.setHorizontalAlignment(JLabel.CENTER);

        label1.setBorder(BorderFactory.createLineBorder(Color.black));
        label2.setBorder(BorderFactory.createLineBorder(Color.black));
        label3.setBorder(BorderFactory.createLineBorder(Color.black));
        label4.setBorder(BorderFactory.createLineBorder(Color.black));
        label5.setBorder(BorderFactory.createLineBorder(Color.black));
        label6.setBorder(BorderFactory.createLineBorder(Color.black));
        label7.setBorder(BorderFactory.createLineBorder(Color.black));

        label1.setOpaque(true);
        label1.setBackground(Color.white);
        label2.setOpaque(true);
        label2.setBackground(Color.white);
        label3.setOpaque(true);
        label3.setBackground(Color.white);
        label4.setOpaque(true);
        label4.setBackground(Color.white);
        label5.setOpaque(true);
        label5.setBackground(Color.white);
        label6.setOpaque(true);
        label6.setBackground(Color.white);
        label7.setOpaque(true);
        label7.setBackground(Color.white);


        this.add(checkBox,new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.add(label1,new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.add(label2,new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.add(label3,new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.add(label4,new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.add(label5,new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.add(label6,new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.add(label7,new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));


    }



    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        checkBox.init(list,isSelected);

        String[] s = value.toString().split("\\?");
        label1.setText(s[0]);
        label2.setText(s[1]);
        label3.setText(s[2]);
        label4.setText(s[3]);
        label5.setText(s[4]);
        label6.setText(s[5]);
        label7.setText(s[6]);

        //System.out.println(value.toString());

        return this;

    }
}
