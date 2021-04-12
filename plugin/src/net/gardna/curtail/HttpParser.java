package net.gardna.curtail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;

/**
 * Class for HTTP request parsing as defined by RFC 2612:
 * <p>
 * Request = Request-Line ; Section 5.1 (( general-header ; Section 4.5 |
 * request-header ; Section 5.3 | entity-header ) CRLF) ; Section 7.1 CRLF [
 * message-body ] ; Section 4.3
 *
 * @author izelaya
 */
public class HttpParser {

    private String _requestLine;
    private Hashtable<String, String> _requestHeaders;
    private StringBuffer _messagetBody;

    public HttpParser() {
        _requestHeaders = new Hashtable<String, String>();
        _messagetBody = new StringBuffer();
    }

    /**
     * Parse and HTTP request.
     *
     * @param request String holding http request.
     * @throws IOException If an I/O error occurs reading the input stream.
     */
    public void parseRequest(String request) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(request));

        setRequestLine(reader.readLine()); // Request-Line ; Section 5.1

        String header = reader.readLine();
        while (header.length() > 0) {
            appendHeaderParameter(header);
            header = reader.readLine();
        }

        String bodyLine = reader.readLine();
        while (bodyLine != null) {
            appendBody(bodyLine);
            bodyLine = reader.readLine();
        }

    }

    /**
     * 5.1 Request-Line The Request-Line begins with a method token, followed by
     * the Request-URI and the protocol version, and ending with CRLF. The
     * elements are separated by SP characters. No CR or LF is allowed except in
     * the final CRLF sequence.
     *
     * @return String with Request-Line
     */
    public String getRequestLine() {
        return _requestLine;
    }

    private void setRequestLine(String requestLine) throws IOException {
        if (requestLine == null || requestLine.length() == 0) {
            throw new IOException("Invalid Request-Line: " + requestLine);
        }
        _requestLine = requestLine;
    }

    private void appendHeaderParameter(String header) throws IOException {
        int idx = header.indexOf(":");
        if (idx == -1) {
            throw new IOException("Invalid Header Parameter: " + header);
        }
        _requestHeaders.put(header.substring(0, idx), header.substring(idx + 1, header.length()));
    }

    /**
     * The message-body (if any) of an HTTP message is used to carry the
     * entity-body associated with the request or response. The message-body
     * differs from the entity-body only when a transfer-coding has been
     * applied, as indicated by the Transfer-Encoding header field (section
     * 14.41).
     *
     * @return String with message-body
     */
    public String getBody() {
        return _messagetBody.toString();
    }

    private void appendBody(String bodyLine) {
        _messagetBody.append(bodyLine).append("\r\n");
    }

    /**
     * For list of available headers refer to sections: 4.5, 5.3, 7.1 of RFC 2616
     *
     * @param headerName Name of header
     * @return String with the value of the header or null if not found.
     */
    public String getHeader(String headerName) {
        return _requestHeaders.get(headerName);
    }

    public Hashtable<String, String> getHeaders() {
        return _requestHeaders;
    }
}