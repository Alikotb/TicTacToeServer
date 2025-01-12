package tictactoeserver.server.handler;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import tictactoeserver.model.UserDao;

public class ClientHandler extends Thread {

    private static UserDao userDao = UserDao.getInstance();
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    @Override
    public void run() {
        try {
            String request = dis.readUTF();
            JsonObject json = Json.createReader(new StringReader(request)).readObject();
            int action = json.getInt("action");

            switch (action) {
                case 1:
                    break;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            saveResources();
        }
    }

    private void saveResources() {
        try {
            dis.close();
            dos.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
