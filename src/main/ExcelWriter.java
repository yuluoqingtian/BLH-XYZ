package main;

import javabean.ExcelItem;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.java2d.pipe.OutlineTextRenderer;

import javax.swing.table.AbstractTableModel;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter {
    public static void main(String[] args) {
        System.out.println("fuck you");
    }

    public static void write(String ExcelPath, AbstractTableModel tableModel) throws IOException, FileNotFoundException {
        //创建workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //添加workSheet ，不添加打开会报错
        XSSFSheet sheet1 = workbook.createSheet("sheet1");

        //新建文件
        FileOutputStream out = null;

        //添加表头,创建第一行
        XSSFRow row = workbook.getSheet("sheet1").createRow(0);
        row.createCell(0).setCellValue("X");
        row.createCell(1).setCellValue("Y");
        row.createCell(2).setCellValue("Z");
        row.createCell(3).setCellValue("B");
        row.createCell(4).setCellValue("L");
        row.createCell(5).setCellValue("H");


        //下面开始写tableModel里面的数据
        //一共有多少列
        for (int rowIndex = 1; rowIndex <= tableModel.getRowCount(); rowIndex++){
            XSSFRow row1 = workbook.getSheet("sheet1").createRow(rowIndex);
            for (int columnIndex=0;columnIndex<tableModel.getColumnCount()-2;columnIndex++){
                //创建第rowIndex行
                System.out.println(rowIndex+"   "+columnIndex);
                row1.createCell(columnIndex).setCellValue(tableModel.getValueAt(rowIndex-1,columnIndex+2).toString());
            }
        }


            out = new FileOutputStream(ExcelPath);
        workbook.write(out);
        out.close();
    }


}
