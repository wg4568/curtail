package net.gardna.curtail.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class MinifyDialogue extends JDialog {
    private JPanel contentPane;

    private JButton buttonOK;
    private JButton buttonCancel;

    private JPanel tablePanel;
    private JScrollPane tableScroll;
    private JTable table;

    private String[] headers;

    public MinifyDialogue(String[] headers) {
        this.headers = headers;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        String[] columnNames = {"Header", "Status"};
        String[][] data = new String[headers.length][2];

        for (int i = 0; i < headers.length; i++) {
            data[i] = new String[]{
                    headers[i],
                    "not tested"
            };
        }

        table = new JTable(data, columnNames);

        TableColumnModel model = table.getColumnModel();
        model.getColumn(0).setPreferredWidth(300);
        model.getColumn(1).setPreferredWidth(100);

        table.setEnabled(false);
        tableScroll = new JScrollPane(table);
        tablePanel.add(tableScroll);

        setTitle("Auto minifier");

        pack();
    }

    public void updateHeader(String header, String status) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < headers.length; i++) {
            if (headers[i] == header) {
                model.removeRow(i);
                model.insertRow(i, new String[]{
                        header,
                        status
                });
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension superPref = super.getPreferredSize();
        return new Dimension(500, (int) superPref.getHeight());
    }
}
