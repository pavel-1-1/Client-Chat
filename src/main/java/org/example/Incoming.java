package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Incoming implements Runnable {

    private final Log log;

    private final BufferedReader in;
    private volatile boolean running = true;

    protected Incoming(Socket clientSocket, Log log) throws IOException {
        this.log = log;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        String readText;

        try {
            while (running) {
                if (in.ready()) {
                    readText = in.readLine();
                    try {
                        System.out.println(readText);
                        if (!readText.equals("Введите имя: max 20 size")) {
                            log.queueAdd(readText);
                        }
                    } catch (InterruptedException ignored) {

                    }
                }
            }
        } catch (IOException e) {
            System.out.println("сервер не доступен!");
        }
    }

    protected void stop() {
        running = false;
    }
}


