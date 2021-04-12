package net.gardna.curtail.minifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class Header {
    public String value;
    public boolean enabled;

    public Header(String value, boolean enabled) {
        this.value = value;
        this.enabled = enabled;
    }
}

public class HttpRequest {
    private Map<String, Header> headers = new HashMap<>();
    private String requestLine;
    private String body;

    private static String Capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static String FormatHeader(String messy) {
        String clean = "";

        for (String word : messy.replace("-", " ").split(" "))
            clean += Capitalize(word) + "-";

        return clean.substring(0, clean.length() - 1);
    }

    public HttpRequest(String raw) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        requestLine = reader.readLine();

        String header = reader.readLine();
        while (header.length() > 0) {
            try {
                String[] seperated = header.split(":");
                setHeader(seperated[0], seperated[1].trim());
                header = reader.readLine();
            } catch (Exception e) {
                throw new IOException("Invalid HTTP request");
            }
        }

        body = "";
        String bodyLine = reader.readLine();
        while (bodyLine != null) {
            body += bodyLine + "\r\n";
            bodyLine = reader.readLine();
        }
    }

    public void setHeader(String key, String value) {
        headers.put(FormatHeader(key), new Header(value, true));
    }

    public String getHeader(String key) {
        return headers.get(FormatHeader(key)).value;
    }

    public void enableHeader(String key) {
        headers.get(FormatHeader(key)).enabled = true;
    }

    public void disableHeader(String key) {
        headers.get(FormatHeader(key)).enabled = false;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public Map<String, Header> getHeaders() {
        return headers;
    }

    public byte[] build() {
        String request = getRequestLine() + "\r\n";

        for (String key : headers.keySet()) {
            Header header = headers.get(key);
            if (header.enabled)
                request += FormatHeader(key) + ": " + header.value + "\r\n";
        }

        request += "\r\n" + body + "\r\n";

        return request.getBytes(StandardCharsets.UTF_8);
    }
}
