package tictactoeserver.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import tictactoeserver.model.User;
import tictactoeserver.model.UserDao;
import tictactoeserver.server.Server;
import tictactoeserver.view.DashBoard;
import tictactoeserver.server.handler.ClientHandler;

public class DashBoardController extends DashBoard {

    private Server server;
    private Thread serverThread;
    private Thread updateChart;
    private boolean running;
    private static ArrayList<User> availableUsers = new ArrayList();
    UserDao uDao;
    public static int[] ar = {0, 0};

    public DashBoardController(Stage stage) {
        uDao = UserDao.getInstance();
        btnController.setOnAction(e -> {
            if (!running) {
                startServer();
            } else {
                stopServer();
                stopBarChart();
            }
        });

        stage.setOnCloseRequest(e -> {
            if (running) {
                stopServer();
                stopBarChart();
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
        startBarchart();
    }

    private void stopServer() {
        running = false;
        if (serverThread != null && serverThread.isAlive()) {

            server.stop();
            for (ClientHandler client : ClientHandler.clients) {
                client.saveResources();
            }
            try {
                serverThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE, null, ex);
            }
            serverThread.interrupt();
        }

        Platform.runLater(() -> {
            btnController.setText("Restart Server");
            btnController.setStyle("-fx-background-color: orange;-fx-text-fill: white;");
        });
    }

    private void startBarchart() {

        updateChart = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    availableUsers = uDao.getAvailableUsers();
                    ClientHandler.sendAvailableUsers();
                    ar[0] = UserDao.getOnlineUsers()[0];
                    ar[1] = UserDao.getOnlineUsers()[1];
                    Platform.runLater(() -> {
                        updateGraph();
                    });
                    Thread.sleep(3000);

                } catch (InterruptedException ex) {
                    updateChart.interrupt();
                    Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        updateChart.start();
    }

    public void updateGraph() {

        dataSeries1.getData().add(new XYChart.Data("Online", ar[0]));
        dataSeries1.getData().add(new XYChart.Data("Offline", ar[1]));
    }

    private void stopBarChart() {
        running = false;
        if (updateChart != null && updateChart.isAlive()) {
            updateChart.interrupt();
        }
    }

    public static ArrayList<User> getAvailableUsers() {
        return availableUsers;
    }

}
