package com.mia.phase10.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private static final int SERVER_PORT = 4001;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream in;


    @Override
    public void run() {
        startServer(SERVER_PORT);

    }


    private void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);


            socket = serverSocket.accept();
            System.out.println("Client accepted");

            // takes input from the client socket
            in = new ObjectInputStream(
                    new BufferedInputStream(socket.getInputStream()));


            while (true) {
                try {
                    Object inObject = in.readObject();
                    System.out.format(inObject.toString());

                } catch (IOException i) {
                    System.out.println(i);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeServer() {
        try {
            serverSocket.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




