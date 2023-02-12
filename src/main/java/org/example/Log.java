package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Log {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>(100);

    private volatile boolean running = true;

    protected Log() {
    }

    protected void queueAdd(String text) throws InterruptedException {
        queue.put(text);
    }

    public void run() {
        Runnable task = () -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("file.log", true))) {
                while (running) {
                    writer.write(queue.take());
                    writer.append('\n');
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(task).start();
    }

    protected void stop() {
        running = false;
    }
}
