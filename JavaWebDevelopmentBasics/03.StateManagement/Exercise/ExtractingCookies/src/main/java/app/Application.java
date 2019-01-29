package main.java.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Application {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {

        try {
            String request = getRequest();
            String cookie = getCookie(request);
            printCookieKvps(cookie);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printCookieKvps(String cookie) {
        if (cookie == null) {
            System.out.println("No cookie found!");
            return;
        }
        Arrays.stream(cookie.split("; "))
                .map(attr -> attr.split("="))
                .forEach(x -> System.out.println(String.format("%s <-> %s", x[0], x[1])));
    }

    private static String getCookie(String request) {
        List<String> lines = Arrays.asList(request.split(System.lineSeparator()));

        String cookie = null;
        for (String line : lines) {
            if (line.startsWith("Cookie: ")) {
                cookie = line.split(": ")[1];
            }
        }
        return cookie;
    }

    private static String getRequest() throws IOException {
        StringBuilder request = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            request.append(line).append(System.lineSeparator());
        }

        request.append(System.lineSeparator());
        if ((line = reader.readLine()) != null && !line.isEmpty()) {
            request.append(line).append(System.lineSeparator());
        }
        return request.toString();
    }
}
