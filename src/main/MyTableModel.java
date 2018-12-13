package main;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {

    private String[] columnNames;
    private Object[][] data;
    private Boolean DEBUG;

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }


    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }


//        JTable使用这个方法来确定默认的渲染器/
//        每个单元格的编辑器。如果我们不实现这个方法，
//        然后最后一列将包含文本(“true”/“false”)，
//        而不是复选框。

    public Class getColumnClass(int c) {
        //System.out.println("getColumnClass"+c);
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col == 1) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {

        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }



    public void selectAllOrNull(boolean value) {
        // Select All. The last column
        for (int index = 0; index < getRowCount(); index++) {
            this.setValueAt(value, index, 0);
        }
    }
}


