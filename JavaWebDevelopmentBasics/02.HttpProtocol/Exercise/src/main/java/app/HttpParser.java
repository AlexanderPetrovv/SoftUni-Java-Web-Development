package main.java.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class HttpParser {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        List<String> validUrls = getValidUrls();
        List<String> request = getRequest();

        String method = getMethod(request.get(0));
        String url = getUrl(request.get(0));

        Map<String, String> headers = getHeaders(request);
        Map<String, String> bodyParams = getBodyParams(request);

        String response = generateResponse(validUrls, method, url, headers, bodyParams);
        System.out.println(response);
    }

    private static String generateResponse(List<String> validUrls, String method,
                                           String url, Map<String,String> headers, Map<String,String> bodyParams) {

        String responseStatusCode = "";
        String responseStatusMessage = "";
        String responseBodyMessage = "";

        StringBuilder response = new StringBuilder();
        if (!validUrls.contains(url)) {
            responseStatusCode = "404";
            responseStatusMessage = "Not Found";
            responseBodyMessage = "The requested functionality was not found.";
        } else if (!headers.containsKey("Authorization")) {
            responseStatusCode = "401";
            responseStatusMessage = "Unauthorized";
            responseBodyMessage = "You are not authorized to access the requested functionality.";
        } else if (method.equals("POST") && bodyParams.isEmpty()) {
            responseStatusCode = "400";
            responseStatusMessage = "Bad Request";
            responseBodyMessage = "There was an error with the requested functionality due to malformed request.";
        } else {
            String encodedUsername = headers.get("Authorization").split("\\s+")[1];
            String username = new String(Base64.getDecoder().decode(encodedUsername), Charset.forName("UTF-8"));

            StringBuilder postBodyMessage = new StringBuilder();
            postBodyMessage.append(" You have successfully created ");

            int counter = 0;
            for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
                if (counter == 0) {
                    postBodyMessage.append(entry.getValue()).append(" with ");
                    counter++;
                    continue;
                }
                postBodyMessage.append(entry.getKey()).append(" - ").append(entry.getValue());
                if (bodyParams.size() - 1 != counter) {
                    postBodyMessage.append(", ");
                } else {
                    postBodyMessage.append(".");
                }
                counter++;
            }

            // Checks only for GET and POST methods
            responseStatusCode = "200";
            responseStatusMessage = "OK";
            responseBodyMessage = String.format("Greetings %s!%s",
                    username,
                    method.equals("GET") ? "" : postBodyMessage.toString());
        }
        createResponseMessage(responseStatusCode, responseStatusMessage, responseBodyMessage, response, headers);
        return response.toString();
    }

    private static void createResponseMessage(
            String statusCode, String statusMsg, String bodyMsg, StringBuilder response, Map<String,String> headers) {
        response.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusMsg).append(System.lineSeparator());
        generateResponseHeaders(headers, response);
        response.append(System.lineSeparator());
        response.append(bodyMsg);
    }

    private static void generateResponseHeaders(Map<String, String> headers, StringBuilder response) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            if (key.equals("Date") || key.equals("Host") || key.equals("Content-Type")) {
                response.append(key).append(": ").append(headers.get(key)).append(System.lineSeparator());
            }
        }
    }


    private static LinkedHashMap<String, String> getBodyParams(List<String> request) {
        if ("\r\n".equals(request.get(request.size() - 1))) {
            return new LinkedHashMap<>();
        }

        List<String> bodyParams = Arrays.asList(request.get(request.size() - 1).split("&"));

        return bodyParams.stream()
                .map(param -> param.split("=", 2))
                .collect(Collectors.toMap(x -> x[0], x -> x[1], (x, y) -> y, LinkedHashMap::new));

    }

    private static LinkedHashMap<String,String> getHeaders(List<String> request) {
        return request
                .stream()
                .skip(1)
                .filter(h -> h.contains(": "))
                .map(l -> l.split(": ", 2))
                .collect(Collectors.toMap(x -> x[0], x -> x[1], (x, y) -> y, LinkedHashMap::new));
    }

    private static String getUrl(String line) {
        return line.split("\\s+")[1];
    }

    private static String getMethod(String line) {
        return line.split("\\s+")[0];
    }

    private static List<String> getValidUrls() throws IOException {
        return Arrays.asList(reader.readLine().split("\\s+"));
    }

    private static List<String> getRequest() throws IOException {
        List<String> request = new ArrayList<>();

        String line;

        while ((line = reader.readLine()) != null && line.length() > 0) {
            request.add(line);
        }

        request.add(System.lineSeparator());
        if ((line = reader.readLine()) != null && line.length() > 0) {
            request.add(line);
        }
        return request;
    }
}
