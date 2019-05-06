package com.mia.phase10.network;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private static final int SERVER_PORT = 4001;
    private Socket socket;
    private ObjectOutputStream out;

    @Override
    public void run() {
        startClient("127.0.0.1", SERVER_PORT);
        sendMessage("Hello World");

    }

    private void startClient(String IP, int port) {
        try {
            socket = new Socket(IP, port);
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void sendMessage(String message) {


        Object obj = new TextTransportObject(message);
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeClient() {
        try {
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
