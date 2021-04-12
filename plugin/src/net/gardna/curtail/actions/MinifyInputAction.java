package net.gardna.curtail.actions;

import burp.IHttpService;
import net.gardna.curtail.Curtail;
import net.gardna.curtail.HttpParser;
import net.gardna.curtail.ui.CurtailTab;
import net.gardna.curtail.ui.MinifyDialogue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Iterator;

public class MinifyInputAction implements ActionListener {
    private Curtail plugin;
    private CurtailTab curtailTab;

    public MinifyInputAction(Curtail plugin, CurtailTab curtailTab) {
        this.plugin = plugin;
        this.curtailTab = curtailTab;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IHttpService service;
        byte[] initial = plugin.getTab().getRequest();

        try {
            service = plugin.getTab().getService();
        } catch (MalformedURLException malformedURLException) {
            plugin.getStderr().println("User provided invalid host");
            JOptionPane.showMessageDialog(null, "Invalid host", "BurpSuite", JOptionPane.ERROR_MESSAGE);
            return;
        }

        HttpParser parser = new HttpParser();

        try {
            parser.parseRequest(new String(initial));
        } catch (IOException ioException) {
            plugin.getStderr().println("User provided invalid request");
            JOptionPane.showMessageDialog(null, "Invalid request", "BurpSuite", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] headers = new String[parser.getHeaders().size()];
        Iterator keys = parser.getHeaders().keys().asIterator();
        int idx = 0;
        while (keys.hasNext()) {
            plugin.getStdout().println(headers[idx]);
            headers[idx] = (String) keys.next();
            idx++;
        }

        Arrays.sort(headers);
        plugin.getStdout().println("Creating dialog with " + headers.length + " headers");
        MinifyDialogue dialogue = new MinifyDialogue(headers);

        dialogue.setVisible(true);
    }
}
