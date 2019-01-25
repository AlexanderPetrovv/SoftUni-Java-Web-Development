package main.java.app.engine;

import main.java.app.http.HttpRequest;
import main.java.app.http.HttpRequestImpl;
import main.java.app.http.HttpResponse;
import main.java.app.http.HttpResponseImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class Engine implements Runnable {

    private BufferedReader reader;

    public Engine(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() throws IOException {
        List<String> validUrls = getValidUrls();
        String request = getRequest();
        HttpRequest httpRequest = new HttpRequestImpl(request);
        HttpResponse httpResponse = new HttpResponseImpl(httpRequest, validUrls);
        System.out.println(new String(httpResponse.getBytes(), Charset.forName("UTF-8")));
    }

    private String getRequest() throws IOException {
        StringBuilder request = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null && line.length() > 0) {
            request.append(line).append(System.lineSeparator());
        }

        request.append(System.lineSeparator());
        if ((line = reader.readLine()) != null && line.length() > 0) {
            request.append(line);
        }
        return request.toString();
    }

    private List<String> getValidUrls() throws IOException {
        return Arrays.asList(this.reader.readLine().split("\\s+"));
    }
}
