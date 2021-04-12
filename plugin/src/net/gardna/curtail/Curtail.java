package net.gardna.curtail;

import burp.IBurpExtenderCallbacks;
import net.gardna.curtail.ui.CurtailTab;

import java.io.PrintWriter;

public class Curtail {
    public static final String TAB_NAME = "Curtail";

    private IBurpExtenderCallbacks callbacks;
    private CurtailTab curtailTab;
    private CurtailMenu curtailMenu;
    private PrintWriter stdout;
    private PrintWriter stderr;

    public void registerCallbacks(IBurpExtenderCallbacks callbacks) {
        callbacks.setExtensionName("Curtail - HTTP Minifier");

        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        this.stderr = new PrintWriter(callbacks.getStderr(), true);

        this.callbacks = callbacks;
        this.curtailTab = new CurtailTab(this, TAB_NAME);
        this.curtailMenu = new CurtailMenu(this);

        curtailTab.register();
        curtailMenu.register();

        getStdout().println("Curtail initialization complete");
    }

    public CurtailTab getTab() {
        return this.curtailTab;
    }

    public PrintWriter getStdout() {
        return this.stdout;
    }

    public PrintWriter getStderr() {
        return this.stderr;
    }

    public IBurpExtenderCallbacks getCallbacks() {
        return callbacks;
    }
}
