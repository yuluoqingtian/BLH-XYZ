package main;

import javabean.ExcelItem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

public class ExcelReader {

    public static void main(String[] args) {
        try {
            read("./新建 Microsoft Excel 工作表.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ExcelItem> read(String ExcelPath) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(ExcelPath);
        XSSFSheet sheet = workbook.getSheet("Sheet1");

        //获取Excel文件中的所有行数
        int rows = sheet.getPhysicalNumberOfRows();

        //遍历行
        //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ArrayList<ExcelItem> itemsList = new ArrayList<ExcelItem>();
        for (int i = 1; i < rows; i++) {
            //读取左上角单元格
            XSSFRow row = sheet.getRow(i);

            //行不为空
            if (row != null) {
                //Map<String, Object> map = new LinkedHashMap<String, Object>();

                //获取Excel文件中所有的列
                int cells = row.getPhysicalNumberOfCells();

                //key
                XSSFCell key_cell = row.getCell(0);
                String key_value = getValue(key_cell);

                //X
                XSSFCell X_cell = row.getCell(1);
                String X_value = getValue(X_cell);

                //Y
                XSSFCell Y_cell = row.getCell(2);
                String Y_value = getValue(Y_cell);

                //X
                XSSFCell Z_cell = row.getCell(3);
                String Z_value = getValue(Z_cell);

                //B
                XSSFCell B_cell = row.getCell(4);
                String B_value = getValue(B_cell);

                //L
                XSSFCell L_cell = row.getCell(5);
                String L_value = getValue(L_cell);

                //H
                XSSFCell H_cell = row.getCell(6);
                String H_value = getValue(H_cell);


                ExcelItem item = new ExcelItem(key_value, X_value, Y_value, Z_value,
                        B_value, L_value, H_value);

                itemsList.add(item);

            }

        }
        //System.out.println(itemsList);
        return itemsList;

    }

    //    在每一行中，通过列名来获取对应列的值
    public static String getValue(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
        //返回布尔类型的值
        else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
            //返回数值类型的值
            return String.valueOf(xssfCell.getNumericCellValue());
        }
        else {
            //返回字符串类型的值
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }

}
