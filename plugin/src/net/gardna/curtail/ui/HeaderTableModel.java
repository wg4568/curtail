package net.gardna.curtail.ui;

import javax.swing.table.AbstractTableModel;

public class HeaderTableModel extends AbstractTableModel {
    String[] columns = {"Header", "Status"};
    String[][] inData = {};

    public HeaderTableModel(String headers[]) {
        inData = new String[headers.length][2];
        for (int i = 0; i < headers.length; i++) {
            inData[i] = new String[]{
                    headers[i],
                    "not tested"
            };
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        inData[row][col] = (String) value;
        fireTableCellUpdated(row, col);
    }

    @Override
    public Object getValueAt(int row, int col) {
        return inData[row][col];
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public int getRowCount() {
        return inData.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    //This method updates the Row of table
    public void updateRow(int index, String[] values) {
        for (int i = 0; i < values.length; i++) {
            setValueAt(values[i], index, i);
        }
    }
}
