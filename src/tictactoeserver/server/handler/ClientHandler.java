package tictactoeserver.server.handler;

import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoeserver.model.UserAction;
import tictactoeserver.model.UserDao;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private UserDao userDao;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.userDao = new UserDao();
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            PrintStream ps = new PrintStream(clientSocket.getOutputStream());

            String request = dis.readLine();
            System.out.println("Received: " + request);

            Gson gson = new Gson();
            UserAction userAction = gson.fromJson(request, UserAction.class);
            String response;

            switch (userAction.getAction()) {
                case 1 :
                    response = userDao.signup(userAction.getUser());
                    break;

                default:
                    response = "error, Invalid action...";
            }

            ps.println(gson.toJson(response));
            ps.flush();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (SQLException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
