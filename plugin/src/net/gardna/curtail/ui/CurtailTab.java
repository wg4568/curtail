package net.gardna.curtail.ui;

import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IMessageEditor;
import burp.ITab;
import net.gardna.curtail.Curtail;
import net.gardna.curtail.minifier.HttpRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CurtailTab implements ITab {
    public static final int EDITOR_TAB = 0;
    public static final int OUTPUT_TAB = 1;

    private Curtail plugin;
    private String title;

    private JPanel panel;
    private JSplitPane basePane;
    private JTabbedPane tabs;
    private JPanel inputTab;
    private JPanel outputTab;
    private JButton minifyButton;
    private JButton curlButton;
    private JTextField hostField;

    private IMessageEditor inputEditor;
    private IMessageEditor outputEditor;

    public CurtailTab(Curtail plugin, String title) {
        this.plugin = plugin;
        this.title = title;
    }

    public void register() {
        this.inputEditor = plugin.getCallbacks().createMessageEditor(null, true);
        this.outputEditor = plugin.getCallbacks().createMessageEditor(null, false);

        this.inputTab.add(inputEditor.getComponent());
        this.outputTab.add(outputEditor.getComponent());

        this.minifyButton.addActionListener(new ActionListener() {
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

                HttpRequest parser;

                try {
                    parser = new HttpRequest(new String(initial));
                } catch (IOException ioException) {
                    plugin.getStderr().println("User provided invalid request");
                    JOptionPane.showMessageDialog(null, "Invalid request", "BurpSuite", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                plugin.getStdout().println("Creating dialog with " + parser.getHeaders().size() + " headers");
                MinifyDialogue dialogue = new MinifyDialogue(plugin, parser);
                dialogue.setVisible(true);
            }
        });

        plugin.getCallbacks().customizeUiComponent(tabs);
        plugin.getCallbacks().addSuiteTab(this);

        plugin.getStdout().println("CurtailTab registered");
    }

    public void setInputEditor(IHttpRequestResponse msg) {
        IHttpService service = msg.getHttpService();

        this.inputEditor.setMessage(msg.getRequest(), true);
        this.hostField.setText(
                service.getProtocol() + "://" +
                        service.getHost() + ":" +
                        service.getPort()
        );
    }

    public IHttpService getService() throws MalformedURLException {
        URL url = new URL(hostField.getText());

        return new IHttpService() {
            @Override
            public String getHost() {
                return url.getHost();
            }

            @Override
            public int getPort() {
                return url.getPort();
            }

            @Override
            public String getProtocol() {
                return url.getProtocol();
            }
        };
    }

    public byte[] getRequest() {
        return this.inputEditor.getMessage();
    }

    public void focus() {
        focus(tabs.getSelectedIndex());
    }

    public void focus(int idx) {
        JTabbedPane burpTabs = (JTabbedPane) basePane.getParent();

        for (int i = 0; i < burpTabs.getTabCount(); i++)
            if (burpTabs.getTitleAt(i).equals(this.getTabCaption()))
                burpTabs.setSelectedIndex(i);

        tabs.setSelectedIndex(idx);
    }

    @Override
    public String getTabCaption() {
        return title;
    }

    @Override
    public Component getUiComponent() {
        return basePane;
    }
}
