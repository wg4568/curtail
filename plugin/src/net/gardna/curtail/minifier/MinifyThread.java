package net.gardna.curtail.minifier;

import burp.IHttpService;
import net.gardna.curtail.Curtail;
import net.gardna.curtail.ui.MinifyDialogue;

import java.net.MalformedURLException;

public class MinifyThread extends Thread {
    private Curtail plugin;
    private MinifyDialogue dialogue;
    private HttpRequest request;

    public MinifyThread(Curtail plugin, MinifyDialogue dialogue, HttpRequest request) {
        this.plugin = plugin;
        this.dialogue = dialogue;
        this.request = request;
    }

    public byte[] makeRequest(IHttpService host, HttpRequest request) {
        return plugin.getCallbacks().makeHttpRequest(
                host.getHost(),
                host.getPort(),
                host.getProtocol() == "https",
                request.build()
        );
    }

    @Override
    public void run() {
        byte[] req = plugin.getTab().getRequest();
        IHttpService host;

        try {
            host = plugin.getTab().getService();
        } catch (MalformedURLException malformedURLException) {
            // TODO: refactor
            plugin.getStderr().println("Invalid host. This should already have been caught");
            return;
        }

        byte[] baseline = makeRequest(host, request);

        plugin.getStdout().println(new String(baseline));
    }
}
