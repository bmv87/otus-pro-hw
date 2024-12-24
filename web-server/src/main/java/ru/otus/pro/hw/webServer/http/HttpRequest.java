package ru.otus.pro.hw.webServer.http;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pro.hw.webServer.helpers.ApplicationPropertiesHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    @Getter
    private final String rawRequest;
    @Getter
    private String protocol;
    @Getter
    private int contentLength;
    @Getter
    private String uri;
    @Getter
    private String path;
    @Getter
    private HttpMethod method;
    @Getter
    private final Map<String, String> parameters = new HashMap<>();
    @Getter
    private final Map<String, String> headers = new HashMap<>();
    @Getter
    private String body;
    @Getter
    private byte[] bodyB;
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    public HttpRequest(InputStream in) throws IOException {
        rawRequest = readRequestInfo(in);
        if (rawRequest == null || rawRequest.isBlank()) {
            return;
        }
        logger.debug("{}{}", System.lineSeparator(), rawRequest);
        this.parse(rawRequest.split("\r\n"));
        readRequestBody(in);
    }

    private void parseContentLength() {
        var contentLen = DEFAULT_BUFFER_SIZE;

        if (headers.containsKey(Constants.Headers.CONTENT_LENGTH.toLowerCase())) {
            contentLen = Integer.parseInt(headers.get(Constants.Headers.CONTENT_LENGTH.toLowerCase()));
        }
        this.contentLength = contentLen;
    }

    public boolean isContentLengthExceeded() {
        var maxContentLength = ApplicationPropertiesHelper.tryGet(ApplicationPropertiesHelper.MAX_CONTENT_LENGTH, Integer.class);

        return maxContentLength < this.contentLength;
    }

    private void readRequestBody(InputStream in) throws IOException {

        if (method != HttpMethod.POST && method != HttpMethod.PUT) {
            return;
        }
        if (isContentLengthExceeded()) {
            in.skip(this.contentLength);
            return;
        }

        byte[] buffer = new byte[this.contentLength];
        int currentStartIndex = 0;
        int totalLength = 0;

        while (totalLength < this.contentLength) {
            int n = in.read();
            if (n < 0) {
                break;
            }
            buffer[currentStartIndex] = (byte) n;
            currentStartIndex = currentStartIndex + 1;
            totalLength = totalLength + 1;
        }
        if (headers.containsKey(Constants.Headers.CONTENT_DISPOSITION.toLowerCase())) {
            bodyB = buffer;
        } else {
            this.body = new String(buffer, StandardCharsets.UTF_8);
        }
    }

    private String readRequestInfo(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        char t1 = '\r';
        char t2 = '\n';
        int b;
        var charCount = 0;
        while ((b = in.read()) != -1) {
            char c = (char) b;

            if (c == t1 || c == t2) {
                charCount = charCount + 1;
            } else {
                charCount = 0;
            }
            sb.append(c);
            if (charCount == 4) {
                break;
            }
        }

        return sb.toString();
    }

    private void parseUriParameters(String url) {
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                var value = keyValue.length > 1 ? keyValue[1] : null;
                this.parameters.put(keyValue[0], value);
            }
        }
    }

    private void parseHeaders(String[] lines) {
        for (int i = 1; i < lines.length; i++) {
            var header = lines[i];
            var keyValue = header.split(": ", 2);
            var value = keyValue.length > 1 ? keyValue[1] : "";
            headers.put(keyValue[0].toLowerCase(), value);
        }

    }

    private void parse(String[] lines) {
        var firstLineParams = lines[0].split(" ");
        logger.info("+++++++++++++++++++++++++++++++++++++");
        logger.info(firstLineParams[0]);
        this.method = HttpMethod.valueOf(firstLineParams[0]);
        this.uri = firstLineParams[1];
        this.protocol = firstLineParams[2];
        this.path = parsePathString(this.uri);
        parseUriParameters(uri);
        parseHeaders(lines);
        parseContentLength();
        logInfo();
    }

    private String parsePathString(String pathString) {

        var pathEndIndex = pathString.indexOf("?");
        if (pathEndIndex == -1) {
            pathEndIndex = pathString.indexOf("/r/n");
        }
        if (pathEndIndex == -1) {
            return pathString.substring(1);
        }
        return pathString.substring(1, pathEndIndex);
    }

    private void logInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator())
                .append("uri: ").append(uri).append(System.lineSeparator())
                .append("method: ").append(method).append(System.lineSeparator());
        if (body != null && !body.isBlank()) {
            sb.append("body: ").append(body).append(System.lineSeparator());
        }
        logger.info(sb.toString());
    }
}
