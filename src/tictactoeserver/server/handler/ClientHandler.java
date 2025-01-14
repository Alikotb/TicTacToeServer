package tictactoeserver.server.handler;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import tictactoeserver.model.User;
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
        while (true) {
            try {
                String request = dis.readUTF();
                JsonObject json = Json.createReader(new StringReader(request)).readObject();
                int action = json.getInt("action");

                switch (action) {
                    case 1:
                        break;
                    case 2:
                        handelLoginRequest(json);
                        break;
                }

            } catch (IOException e) {
                stop();
            }
        }
    }

    private void handelLoginRequest(JsonObject json) throws IOException {
        String username = json.getString("username");
        String password = json.getString("password");

        User user = new User(username, password);
        User loggedInUser = userDao.updateUser(user);

        if (loggedInUser != null) {
            JsonObject response = Json.createObjectBuilder()
                    .add("action", 2)
                    .add("status", "success")
                    .add("username", loggedInUser.getUsername())
                    .add("password", loggedInUser.getPassword())
                    .add("score", loggedInUser.getScore())
                    .build();
            dos.writeUTF(response.toString());
        } else {
            JsonObject errorResponse = Json.createObjectBuilder()
                    .add("action", 2)
                    .add("status", "failure")
                    .add("message", "Invalid username or password")
                    .build();
            dos.writeUTF(errorResponse.toString());

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
