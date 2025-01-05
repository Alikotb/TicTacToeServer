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
import tictactoeserver.ui.Server;

/**
 *
 * @author medos
 */
public class TicTacToeServer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = new Server();

        Scene scene = new Scene(root, 600,500);

        stage.setScene(scene);
        stage.show();
        stage.setTitle("SERVER");

//        Server server = new Server();
//
//
//        CategoryAxis xAxis    = new CategoryAxis();
//        xAxis.setLabel("USERS");
//
//        NumberAxis yAxis = new NumberAxis();
//        yAxis.setLabel("COUNT");
//
//        BarChart  barChart = new BarChart(xAxis, yAxis);
//
//        XYChart.Series dataSeries1 = new XYChart.Series();
////        dataSeries1.setName("2020");
//
//        dataSeries1.getData().add(new XYChart.Data("Online", 2));
//        dataSeries1.getData().add(new XYChart.Data("Offline"  , 5));
////        dataSeries1.getData().add(new XYChart.Data("USA"  , 330));
//        dataSeries1.getData().add(new XYChart.Data("Indonesia", 272));
//        dataSeries1.getData().add(new XYChart.Data("Pakistan"  , 219));
//        dataSeries1.getData().add(new XYChart.Data("Brazil"  , 212));
//        dataSeries1.getData().add(new XYChart.Data("Nigeria", 204));
//        dataSeries1.getData().add(new XYChart.Data("Bangladesh"  , 164));
//        dataSeries1.getData().add(new XYChart.Data("Russia"  , 145));
//        dataSeries1.getData().add(new XYChart.Data("Mexico"  , 128));
//        barChart.getData().add(dataSeries1);
//
//
//        Scene scene = new Scene(vbox, 800, 600);
//        primaryStage.setScene(scene);
//        primaryStage.setHeight(600);
//        primaryStage.setWidth(800);
//
//        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
