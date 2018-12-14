package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame("mainWindow");
        Main_win2 win = new Main_win2();
        frame.setContentPane(win.panel1);
        frame.setBounds(100, 100, 500, 100);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
         * 创建一个菜单栏
         */




        frame.setJMenuBar(win.jMenuBar);

        frame.pack();
        frame.setVisible(true);
    }
}
