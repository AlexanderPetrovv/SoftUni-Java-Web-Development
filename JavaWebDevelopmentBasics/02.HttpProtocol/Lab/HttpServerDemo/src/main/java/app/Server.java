package main.java.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port;
    private ServerSocket tcpListener;

    public Server(int port) {
        this.port = port;
    }

    // Response example
    private String getResponse() {
        return "HTTP/1.1 200 OK" + System.lineSeparator() +
                "Host: SoftUni Server 2019" + System.lineSeparator() +
                "Content-Type: text/html" + System.lineSeparator() +
                System.lineSeparator() +
                "<center><h1>Hello, World!</h1></center>";
    }

    public void run() throws IOException {
        this.tcpListener = new ServerSocket(this.port);

        System.out.println("Listening on: http://localhost:" + this.port);

        while(true) {
            Socket clientConnection = this.tcpListener.accept();
            System.out.println("CLIENT CONNECTED!");

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));

            StringBuilder requestContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null && line.length() > 0) {
                requestContent.append(line).append(System.lineSeparator());
            }
            System.out.println(requestContent.toString());

            OutputStream outputStream = clientConnection.getOutputStream();
            outputStream.write(this.getResponse().getBytes());

            reader.close();
            outputStream.close();
        }
    }
}
