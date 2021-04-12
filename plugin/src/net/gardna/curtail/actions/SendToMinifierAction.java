package net.gardna.curtail.actions;

import burp.IContextMenuInvocation;
import burp.IHttpRequestResponse;
import net.gardna.curtail.Curtail;
import net.gardna.curtail.ui.CurtailTab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendToMinifierAction implements ActionListener {
    private IContextMenuInvocation invocation;
    private Curtail plugin;

    public SendToMinifierAction(Curtail plugin, IContextMenuInvocation invocation) {
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