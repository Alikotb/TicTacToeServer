package tictactoeserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoeserver.server.handler.ClientHandler;

public class Server {
    private static final int PORT = 55555;
    private ServerSocket serverSocket;
    
    
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running on Port: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected >> " + clientSocket.getLocalSocketAddress());
                new ClientHandler(clientSocket).start();
                Thread.sleep(1000);
            }
        } catch (IOException e) {
                e.printStackTrace();
           
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            
        } finally {
            stop();
        }
    }

    public void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server socket closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
