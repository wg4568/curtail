package net.gardna.curtail.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class LabelCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object oValue, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, oValue, isSelected, hasFocus, row, column);
        String value = (String) oValue;
        JLabel label = (JLabel) c;

        label.setHorizontalTextPosition(SwingUtilities.LEFT);

        label.setText(value);
        return label;
    }
}
