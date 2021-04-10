package net.gardna.curtail;

import burp.IContextMenuInvocation;
import burp.IHttpRequestResponse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinifyAction implements ActionListener {
    private IContextMenuInvocation invocation;
    private Curtail plugin;

    public MinifyAction(Curtail plugin, IContextMenuInvocation invocation) {
        this.plugin = plugin;
        this.invocation = invocation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IHttpRequestResponse msg = invocation.getSelectedMessages()[0];

        plugin.getTab().setInputEditor(msg);
        plugin.getTab().focus(CurtailTab.EDITOR_TAB);
    }
}