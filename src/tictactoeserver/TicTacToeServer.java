/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tictactoeserver.controller.DashBoardController;
import tictactoeserver.view.DashBoard;

/**
 *
 * @author medos
 */
public class TicTacToeServer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = new DashBoardController(stage);
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("SERVER");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
