package net.gardna.curtail;

import burp.IHttpRequestResponse;
import burp.IHttpService;
import burp.IMessageEditor;
import burp.ITab;

import javax.swing.*;
import java.awt.*;
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
