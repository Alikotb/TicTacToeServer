package tictactoeserver.server.handler;

import java.io.*;
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
    private static ArrayList<ClientHandler> clients = new ArrayList();

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
        while (true) {
            try {
                String request = dis.readUTF();
                JsonObject json = Json.createReader(new StringReader(request)).readObject();
                int action = json.getInt("action");

                switch (action) {
                    case 1:
                        System.out.println("Sign Up Request Case");
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
                        handelLogout(json);
                        break;
                    }

                }

            } catch (IOException e) {
                saveResources();
                stop();
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

    private void handelLogout(JsonObject json) throws IOException {
        String username = json.getString("username");
        User user = new User(username, false, false);
        userDao.logOut(user);
        JsonObject response = Json.createObjectBuilder()
                .add("action", 6)
                .add("status", "success")
                .build();
        dos.writeUTF(response.toString());

    }

    private void handleSendInvitationRequest(JsonObject json) {
        String player1 = json.getString("username-player1");
        String player2 = json.getString("username-player2");
        int status = json.getInt("status");

        // TODO check if user is online and available
        DataOutputStream clientDos;
        if (status == 2) {
            clientDos = getClientDos(player1);
        } else {
            clientDos = getClientDos(player2);
        }

        if (clientDos != null) {
            try {

                if (status == 2) { // player 2 accepted invitation
                    System.out.println("Server / player 2 accepted invitation");
                    String jsonStartGame = Json.createObjectBuilder()
                            .add("action", 4)
                            .add("status", 4) // declined
                            .add("username-player2", player2)
                            .add("username-player1", player1)
                            .add("score-player1", json.getInt("score-player1"))
                            .add("score-player2", json.getInt("score-player2"))
                            .build().toString();
                    dos.writeUTF(jsonStartGame);
                }
                clientDos.writeUTF(json.toString());
                System.out.println("Client receive response");
            } catch (IOException ex) {
                // TODO couldn't invite response to client
            }
        } else {
//            dos.writeUTF(); // TODO
            System.out.println("Client didn't receive response");
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
            System.out.print(c.username + ", ");

            if (username.equals(c.username) && !this.username.equals(c.username)) {
                // TODO username.isavailable() -> true
                return c.dos;
            }
        }
        return null;
    }

    public static void getAvailableUsers() throws IOException {
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
            client.dos.writeUTF(errorResponse.toString());
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
