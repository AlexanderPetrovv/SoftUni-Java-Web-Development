package main.java.app.http;

import java.util.*;

public class HttpRequestImpl implements HttpRequest {

    private String method;
    private String requestUrl;
    private Map<String, String> headers;
    private Map<String, String> bodyParameters;

    public HttpRequestImpl(String request) {
        this.headers = new LinkedHashMap<>();
        this.bodyParameters = new LinkedHashMap<>();
        process(request);
    }

    private void process(String request) {
        List<String> lines = List.of(request.split("\r\n", -1));

        // Request line parse
        String requestLine = lines.get(0);
        String[] requestLineParams = requestLine.split("\\s+");
        this.setMethod(requestLineParams[0]);
        this.setRequestUrl(requestLineParams[1]);

        // Request headers parse
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).isEmpty()) {
                break;
            }
            String[] headersParams = lines.get(i).split(": ");
            this.addHeader(headersParams[0], headersParams[1]);
        }

        // Request body parse
        if (!lines.get(lines.size() - 1).isEmpty()) {
            Arrays.stream(lines.get(lines.size() - 1).split("&"))
                    .map(param -> param.split("=", 2))
                    .forEach(kvp -> this.addBodyParameter(kvp[0], kvp[1]));
        }
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    @Override
    public Map<String, String> getBodyParameters() {
        return Collections.unmodifiableMap(this.bodyParameters);
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getRequestUrl() {
        return this.requestUrl;
    }

    @Override
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Override
    public void addHeader(String header, String value) {
        this.headers.putIfAbsent(header, value);
    }

    @Override
    public void addBodyParameter(String parameter, String value) {
        this.bodyParameters.putIfAbsent(parameter, value);
    }

    @Override
    public boolean isResource() {
        return this.requestUrl.contains(".");
    }
}
