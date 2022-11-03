package server;

import engine.Engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int TCP_PORT = 8080;

    public static void main(String[] args) {
        try {
            Engine.initClasses();
            ServerSocket serverSocket = new ServerSocket(TCP_PORT);
            System.out.println("Server is running at http://localhost:" + TCP_PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ServerThread(socket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
