package net.gardna.curtail.ui;

import net.gardna.curtail.Curtail;
import net.gardna.curtail.minifier.HttpRequest;
import net.gardna.curtail.minifier.MinifyThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;


public class MinifyDialogue extends JDialog {
    private JPanel contentPane;

    private JButton buttonTest;
    private JButton buttonApply;

    private JPanel tablePanel;
    private JScrollPane tableScroll;
    private JTable table;

    private String[] headers;

    public MinifyDialogue(Curtail plugin, HttpRequest request) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonTest);

        this.headers = new String[request.getHeaders().size()];

        int idx = 0;
        for (String key : request.getHeaders().keySet()) {
            headers[idx] = key;
            idx++;
        }

        Arrays.sort(headers);

        HeaderTableModel model = new HeaderTableModel(headers);
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setCellRenderer(new LabelCellRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(new LabelCellRenderer());
        table.setEnabled(false);

        tableScroll = new JScrollPane(table);
        tablePanel.add(tableScroll);

        setTitle("Auto minifier");

        buttonTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MinifyThread thread = new MinifyThread(plugin, MinifyDialogue.this, request);
                thread.start();
            }
        });

        pack();
    }

    public void updateHeader(String header, String status) {
        HeaderTableModel model = (HeaderTableModel) table.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(header))
                model.setValueAt(status, i, 1);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension superPref = super.getPreferredSize();
        return new Dimension(500, (int) superPref.getHeight());
    }
}
