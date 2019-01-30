package main.java.app.http;

import java.nio.charset.Charset;
import java.util.*;

public class HttpResponseImpl implements HttpResponse {

    private Map<String, String> headers;
    private int statusCode;
    private byte[] content;
    private HttpRequest httpRequest;
    private List<String> validUrls;

    public HttpResponseImpl(HttpRequest httpRequest, List<String> validUrls) {
        this.headers = new LinkedHashMap<>();
        this.httpRequest = httpRequest;
        this.validUrls = validUrls;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public byte[] getContent() {
        return this.content.clone();
    }

    @Override
    public byte[] getBytes() {
        return generateResponse().getBytes();
    }

    @Override
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public void setContent(byte[] content) {
        this.content = content.clone();
    }

    @Override
    public void addHeader(String header, String value) {
        this.headers.putIfAbsent(header, value);
    }

    private String generateResponse() {

        String responseStatusMessage;
        String responseBodyMessage;

        StringBuilder response = new StringBuilder();
        if (!validUrls.contains(this.httpRequest.getRequestUrl())) {
            this.setStatusCode(404);
            responseStatusMessage = "Not Found";
            responseBodyMessage = "The requested functionality was not found.";
        } else if (!this.httpRequest.getHeaders().containsKey("Authorization")) {
            this.setStatusCode(401);
            responseStatusMessage = "Unauthorized";
            responseBodyMessage = "You are not authorized to access the requested functionality.";
        } else if (this.httpRequest.getMethod().equals("POST") && this.httpRequest.getBodyParameters().isEmpty()) {
            this.setStatusCode(400);
            responseStatusMessage = "Bad Request";
            responseBodyMessage = "There was an error with the requested functionality due to malformed request.";
        } else {
            String encodedUsername = this.httpRequest.getHeaders().get("Authorization").split("\\s+")[1];
            String username = new String(Base64.getDecoder().decode(encodedUsername), Charset.forName("UTF-8"));

            StringBuilder postBodyMessage = new StringBuilder();
            postBodyMessage.append(" You have successfully created ");

            int counter = 0;
            for (Map.Entry<String, String> entry : this.httpRequest.getBodyParameters().entrySet()) {
                if (counter == 0) {
                    postBodyMessage.append(entry.getValue()).append(" with ");
                    counter++;
                    continue;
                }
                postBodyMessage.append(entry.getKey()).append(" - ").append(entry.getValue());
                if (this.httpRequest.getBodyParameters().size() - 1 != counter) {
                    postBodyMessage.append(", ");
                } else {
                    postBodyMessage.append(".");
                }
                counter++;
            }

            // Checks only for GET and POST methods
            this.setStatusCode(200);
            responseStatusMessage = "OK";
            responseBodyMessage = String.format("Greetings %s!%s",
                    username,
                    this.httpRequest.getMethod().equals("GET") ? "" : postBodyMessage.toString());
        }
        createResponseMessage(responseStatusMessage, responseBodyMessage, response);
        return response.toString();
    }

    private void createResponseMessage(String statusMsg, String bodyMsg, StringBuilder response) {
        response.append("HTTP/1.1 ").append(this.statusCode).append(" ").append(statusMsg).append(System.lineSeparator());
        generateResponseHeaders(response);
        response.append(System.lineSeparator());
        response.append(bodyMsg);
    }

    private void generateResponseHeaders(StringBuilder response) {
        for (Map.Entry<String, String> entry : this.httpRequest.getHeaders().entrySet()) {
            String key = entry.getKey();
            if (key.equals("Date") || key.equals("Host") || key.equals("Content-Type")) {
                response.append(key).append(": ").append(this.httpRequest.getHeaders().get(key)).append(System.lineSeparator());
            }
        }
    }
}
