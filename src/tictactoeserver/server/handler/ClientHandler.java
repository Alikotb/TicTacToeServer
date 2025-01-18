package tictactoeserver.server.handler;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import tictactoeserver.model.User;
import tictactoeserver.model.UserDao;

public class ClientHandler extends Thread {

    private String username;
    private static UserDao userDao = UserDao.getInstance();
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket clientSocket;
    public static ArrayList<ClientHandler> clients = new ArrayList();

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        clients.add(this);
        try {
            dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {

            if (clientSocket.isClosed()) {
                clients.remove(this);
                handelLogout(username);
                interrupt();
            }

            try {
                String request = dis.readUTF();
                JsonObject json = Json.createReader(new StringReader(request)).readObject();
                int action = json.getInt("action");

                switch (action) {
                    case 1:
                        handelSignupRequest(json);
                        break;
                    case 2:
                        handelLoginRequest(json);
                        break;
                    case 4: {
                        handleSendInvitationRequest(json);
                        break;
                    }
                    case 5: {
                        handleSendMove(json);
                        break;
                    }
                    case 6: {
                        String username = json.getString("username");
                        handelLogout(username);
                        break;
                    }
                    case 7: {
                        handleScoreUpdateRequest(json);
                        break;
                    
                    }
                    

                }

            } catch (IOException e) {
                saveResources();
            }
        }
    }

    private void handelSignupRequest(JsonObject json) throws IOException {
        String username = json.getString("username");
        String email = json.getString("email");
        String password = json.getString("password");
        User user = new User(username, email, password);
        boolean signupStatus = userDao.signup(user);

        if (signupStatus) {
            JsonObject response = Json.createObjectBuilder()
                    .add("action", 1)
                    .add("status", "success")
                    .add("message", "Signed up successfully.")
                    .build();
            dos.writeUTF(response.toString());
        } else {
            JsonObject response = Json.createObjectBuilder()
                    .add("action", 1)
                    .add("status", "failure")
                    .add("message", "Signup failed.")
                    .build();
            dos.writeUTF(response.toString());
        }

    }

    private void handelLoginRequest(JsonObject json) throws IOException {
        try {
            String email = json.getString("email");
            String password = json.getString("password");

            if (userDao.checkEmail(email) == null) {
                sendErrorResponse("Invalid email address");
                return;
            }

            if (!userDao.checkPassword(email).equals(password)) {
                sendErrorResponse("Invalid password");
                return;
            }

            User user = new User(email, password);
            User loggedInUser = userDao.updateUser(user);

            if (loggedInUser != null) {
                loggedInUser.setUsername(userDao.getUserNameByEmail(email));
                loggedInUser.setScore(userDao.getScoreByEmail(email));

                username = loggedInUser.getUsername();
                JsonObject response = Json.createObjectBuilder()
                        .add("action", 2)
                        .add("status", "success")
                        .add("username", loggedInUser.getUsername())
                        .add("score", loggedInUser.getScore())
                        .add("message", "success")
                        .build();
                dos.writeUTF(response.toString());
            } else {
                sendErrorResponse("Failed to log in the user. Please try again.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, "Database error", ex);
        }
    }

    private void sendErrorResponse(String message) throws IOException {
        JsonObject errorResponse = Json.createObjectBuilder()
                .add("action", 2)
                .add("status", "failure")
                .add("message", message)
                .build();
        dos.writeUTF(errorResponse.toString());
    }

    private void handelLogout(String username) {
        try {
            User user = new User(username, false, false);
            userDao.logOut(user);
            JsonObject response = Json.createObjectBuilder()
                    .add("action", 6)
                    .add("status", "success")
                    .build();
            dos.writeUTF(response.toString());
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void handleSendInvitationRequest(JsonObject json) {
        String player1 = json.getString("username-player1");
        String player2 = json.getString("username-player2");
        int status = json.getInt("status");     // 1
        int code = 0;

        if (status == 2) {
            DataOutputStream clientDos = null;
            if (isAvailable(player1)) {
                code = 1;
                clientDos = getClientDos(player1);
                userDao.setIsNotAvailable(player2);
            } else if (isOnline(player1)) {
                code = 2;
            } else {
                code = 3;
            }
            String jsonStartGame = createJsonForAcceptResponse(player2, player1, json, code);
            sendAcceptResponse(json, clientDos, jsonStartGame);
        } else if (status == 3) {
            sendResponse(player1, json);
        } else {    // 1
            sendResponse(player2, json);
        }

    }

    private static boolean isOnline(String player1) {
        return !userDao.isAvailable(player1) && userDao.isOnline(player1);
    }

    private static boolean isAvailable(String player1) {
        return userDao.isAvailable(player1) && userDao.isOnline(player1);
    }

    private String createJsonForAcceptResponse(String player2, String player1, JsonObject json, int code) {
        String jsonStartGame = Json.createObjectBuilder()
                .add("action", 4)
                .add("status", 4)
                .add("username-player2", player2)
                .add("username-player1", player1)
                .add("score-player1", json.getInt("score-player1"))
                .add("score-player2", json.getInt("score-player2"))
                .add("code", code)
                .build().toString();
        return jsonStartGame;
    }

    private void sendResponse(String player2, JsonObject json) {
        DataOutputStream clientDos = getClientDos(player2);
        try {
            clientDos.writeUTF(json.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendAcceptResponse(JsonObject json, DataOutputStream clientDos, String jsonStartGame) {
        try {
            dos.writeUTF(jsonStartGame);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (clientDos != null) {
            try {
                clientDos.writeUTF(json.toString());
                userDao.setIsNotAvailable(json.getString("username-player1"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleSendMove(JsonObject json) {
        DataOutputStream clientDos;
        if (json.getBoolean("isX")) {
            clientDos = getClientDos(json.getString("username-player2"));
        } else {
            clientDos = getClientDos(json.getString("username-player1"));
        }

        if (clientDos != null) {
            try {
                clientDos.writeUTF(json.toString());
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private DataOutputStream getClientDos(String username) {
        for (ClientHandler c : clients) {

            if (username.equals(c.username) && !this.username.equals(c.username)) {
                return c.dos;
            }
        }
        return null;
    }

    public static void sendAvailableUsers()  {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (User user : userDao.getAvailableUsers()) {
            JsonObject userJson = Json.createObjectBuilder()
                    .add("username", user.getUsername())
                    .add("score", user.getScore())
                    .build();
            arrayBuilder.add(userJson);
        }
        JsonArray jsonArray = arrayBuilder.build();
        JsonObject errorResponse = Json.createObjectBuilder()
                .add("action", 3)
                .add("items", jsonArray)
                .build();
        for (ClientHandler client : clients) {
            try {
                client.dos.writeUTF(errorResponse.toString());
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    private void handleScoreUpdateRequest(JsonObject json){
        try {
            String username = json.getString("username");
            int newScore = json.getInt("score");
            
            boolean updateStatus = userDao.updateScore(username, newScore);
           JsonObject response = Json.createObjectBuilder()
                .add("action", 7)
                .add("status", updateStatus ? "success" : "failure")
                .build();
            try {
                dos.writeUTF(response.toString());
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
         
        } catch (SQLException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }

    private void saveResources() {
        try {
            dis.close();
            dos.close();
            clientSocket.close();

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }
}
