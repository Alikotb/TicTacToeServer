package tictactoeserver.view;

import java.awt.Color;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class DashBoard extends FlowPane {

    protected final HBox hBox;
    protected final Button btnController;
    protected final CategoryAxis categoryAxis;
    protected final NumberAxis numberAxis;
    protected final BarChart barChart;
    protected XYChart.Series dataSeries1;

    public DashBoard() {

        hBox = new HBox();
        btnController = new Button();
        categoryAxis = new CategoryAxis();
        numberAxis = new NumberAxis();
        barChart = new BarChart(categoryAxis, numberAxis);

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setOrientation(javafx.geometry.Orientation.VERTICAL);
        setPrefHeight(600.0);
        setPrefWidth(800.0);

        hBox.setAlignment(javafx.geometry.Pos.CENTER);
        hBox.setPrefHeight(100.0);
        hBox.setPrefWidth(600.0);

        btnController.setMnemonicParsing(false);
        btnController.setPrefWidth(220);
        btnController.setText("Start Server");
        btnController.setFont(new Font(24.0));
        btnController.setCursor(Cursor.HAND);
        btnController.setStyle("-fx-background-color:green; -fx-text-fill: white");

        categoryAxis.setSide(javafx.geometry.Side.BOTTOM);

        numberAxis.setSide(javafx.geometry.Side.LEFT);
        barChart.setPrefWidth(200.0);

        dataSeries1 = new XYChart.Series();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("USERS");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("COUNT");
        BarChart barChart = new BarChart(xAxis, yAxis);
       
        barChart.getData().add(dataSeries1);

        hBox.getChildren().add(btnController);
        getChildren().add(hBox);
        getChildren().add(barChart);

    }
}
