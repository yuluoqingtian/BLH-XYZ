package main;

public class MianTest {
    public static void main(String[] args) {
        Object[][] rowData = {
                {"张三", 80, 80, 80, 240},
                {"John", 70, 80, 90, 240},
                {"Sue", 70, 70, 70, 210},
                {"Jane", 80, 70, 60, 210},
                {"Joe", 80, 70, 60, 210}
        };
        System.out.println(rowData[0][4]);
    }
}
