package net.gardna.curtail;

import burp.IContextMenuFactory;
import burp.IContextMenuInvocation;

import javax.swing.*;
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
            sendToMinifier.addActionListener(new MinifyAction(plugin, invocation));

            items.add(sendToMinifier);
        }

        return items;
    }

    public void register() {
        plugin.getCallbacks().registerContextMenuFactory(this);

        plugin.getStdout().println("CurtailMenu registered");
    }
}
