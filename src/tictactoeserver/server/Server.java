package tictactoeserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import tictactoeserver.server.handler.ClientHandler;

public class Server {

    private ServerSocket serverSocket;
    private boolean isRunning = true;

    public void start() {
        try {
            serverSocket = new ServerSocket(55555);
            while (!Thread.interrupted()) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
