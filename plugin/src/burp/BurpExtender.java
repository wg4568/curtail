package burp;

import net.gardna.curtail.Curtail;

public class BurpExtender implements IBurpExtender {
    private Curtail curtail = new Curtail();

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        curtail.registerCallbacks(callbacks);
    }
}