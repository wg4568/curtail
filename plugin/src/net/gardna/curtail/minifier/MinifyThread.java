package net.gardna.curtail.minifier;

import burp.IHttpService;
import net.gardna.curtail.Curtail;
import net.gardna.curtail.ui.MinifyDialogue;

import java.io.IOException;
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

        String baseline = "";
        try {
            baseline = new HttpRequest(new String(makeRequest(host, request))).getBody();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String key : request.getHeaders().keySet()) {
            plugin.getStdout().println("Testing header: " + key);

            request.disableHeader(key);
            String testCase = "";
            try {
                testCase = new HttpRequest(new String(makeRequest(host, request))).getBody();
            } catch (IOException e) {
                e.printStackTrace();
            }
            request.enableHeader(key);

            plugin.getStdout().println(key);
            plugin.getStdout().println(baseline);
            plugin.getStdout().println(testCase);
            plugin.getStdout().println("===================");
            if (baseline.equals(testCase)) {
                dialogue.updateHeader(key, "not required");
            } else {
                dialogue.updateHeader(key, "required");
            }
        }
    }
}
