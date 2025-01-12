package tictactoeserver.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.stage.Stage;
import tictactoeserver.server.Server;
import tictactoeserver.view.DashBoard;

public class DashBoardController extends DashBoard {

    private Server server;
    private Thread serverThread;
    private boolean running;

    public DashBoardController(Stage stage) {

        btnController.setOnAction(e -> {
            if (!running) {
                startServer();
            } else {
                stopServer();
            }
        });

        stage.setOnCloseRequest(e -> {
            if (running) {
                stopServer();
            }
        });
    }

    private void startServer() {
        running = true;
        serverThread = new Thread(() -> {
            server = new Server();
            server.start();
        });
        serverThread.start();

        Platform.runLater(() -> {
            btnController.setText("Stop Server");
            btnController.setStyle("-fx-background-color: red;-fx-text-fill: white;");
        });
    }

    private void stopServer() {
        running = false;
        if (serverThread != null && serverThread.isAlive()) {
            server.stop(); 
            serverThread.stop();
        }
        
        Platform.runLater(() -> {
            btnController.setText("Restart Server");
            btnController.setStyle("-fx-background-color: orange;-fx-text-fill: white;");
        });
    }

}
