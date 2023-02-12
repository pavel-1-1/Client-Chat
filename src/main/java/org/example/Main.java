package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final SimpleDateFormat format = new SimpleDateFormat("d.M.yy HH.mm");

        Log log = new Log();
        log.run();

        Scanner input = new Scanner(System.in);

        Socket clientSocket;
        PrintWriter out;
        String host = parseConfig("localhost");
        int port = Integer.parseInt(parseConfig("port"));

        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("сервер не доступен");
            log.stop();
            log.queueAdd(format.format(new Date()) + " server error");
            return;
        }

        Incoming incoming = new Incoming(clientSocket, log);
        new Thread(incoming).start();

        boolean clientStart = true;
        while (clientStart) {
            String nextLine = input.nextLine();
            if (nextLine.length() > 100) {
                System.out.println("err! max 100 size");
                continue;
            }

            if (nextLine.equals("exit")) {
                out.println(nextLine);
                log.stop();
                incoming.stop();
                log.queueAdd(format.format(new Date()) + " " + nextLine);
                clientStart = false;
            } else {
                out.println(nextLine);
            }
        }
    }

    public static String parseConfig(String str) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            Object obj = parser.parse(new FileReader("config.json"));
            jsonObject = (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return (String) jsonObject.get(str);
    }
}