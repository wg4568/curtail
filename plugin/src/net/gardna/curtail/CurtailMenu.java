package net.gardna.curtail;

import burp.IContextMenuFactory;
import burp.IContextMenuInvocation;
import burp.IHttpRequestResponse;
import net.gardna.curtail.ui.CurtailTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CurtailMenu implements IContextMenuFactory {
    private Curtail plugin;

    public CurtailMenu(Curtail plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {
        List<JMenuItem> items = new ArrayList<>();

        if (invocation.getSelectedMessages().length == 1) {
            JMenuItem sendToMinifier = new JMenuItem("Minify request");

            sendToMinifier.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    IHttpRequestResponse msg = invocation.getSelectedMessages()[0];

                    plugin.getTab().setInputEditor(msg);
                    plugin.getTab().focus(CurtailTab.EDITOR_TAB);
                }
            });

            items.add(sendToMinifier);
        }

        return items;
    }

    public void register() {
        plugin.getCallbacks().registerContextMenuFactory(this);

        plugin.getStdout().println("CurtailMenu registered");
    }
}
