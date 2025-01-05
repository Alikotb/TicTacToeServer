package tictactoeserver.ui;

import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

public class Server extends FlowPane {

    protected final HBox hBox;
    protected final Button button;
    protected final Region region;
    protected final Button button0;
    protected final CategoryAxis categoryAxis;
    protected final NumberAxis numberAxis;
    protected final BarChart barChart;

    public Server() {

        hBox = new HBox();
        button = new Button();
        region = new Region();
        button0 = new Button();
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

        hBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hBox.setPrefHeight(100.0);
        hBox.setPrefWidth(600.0);

        button.setMnemonicParsing(false);
        button.setPrefWidth(100.0);
        button.setText("Start");
        HBox.setMargin(button, new Insets(0.0, 0.0, 0.0, 100.0));
        button.setFont(new Font(24.0));

        HBox.setHgrow(region, javafx.scene.layout.Priority.ALWAYS);
        region.setPrefHeight(1.0);
        region.setPrefWidth(1.0);

        button0.setMnemonicParsing(false);
        button0.setPrefWidth(100.0);
        button0.setText("Stop");
        button0.setFont(new Font(24.0));
        HBox.setMargin(button0, new Insets(0.0, 100.0, 0.0, 0.0));

        categoryAxis.setSide(javafx.geometry.Side.BOTTOM);

        numberAxis.setSide(javafx.geometry.Side.LEFT);
        barChart.setPrefWidth(200.0);

        XYChart.Series dataSeries1 = new XYChart.Series();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("USERS");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("COUNT");
        BarChart barChart = new BarChart(xAxis, yAxis);
        dataSeries1.getData().add(new XYChart.Data("Online", 2));
        dataSeries1.getData().add(new XYChart.Data("Offline", 5));
         barChart.getData().add(dataSeries1);

        hBox.getChildren().add(button);
        hBox.getChildren().add(region);
        hBox.getChildren().add(button0);
        getChildren().add(hBox);
        getChildren().add(barChart);

    }
}
