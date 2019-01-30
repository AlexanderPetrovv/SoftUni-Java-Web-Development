package main.java.app;

import main.java.app.engine.Engine;
import main.java.app.engine.Runnable;

import java.io.*;

public class Application {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Runnable engine = new Engine(reader);
            engine.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
