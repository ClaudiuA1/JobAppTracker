package com.example.jobapplicationmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;

public class ChartService {
    private final PieChart resultChart;
    private final BarChart<String, Number> applicationsChart;

    private final ObservableList<PieChart.Data> pieData;
    private final XYChart.Series<String, Number> barSeries;

    public ChartService(PieChart resultChart, BarChart<String, Number> applicationsChart) {
        this.resultChart = resultChart;
        this.applicationsChart = applicationsChart;

        // Inițializare PieChart
        pieData = FXCollections.observableArrayList(
                new PieChart.Data("Accepted", 0),
                new PieChart.Data("Rejected", 0),
                new PieChart.Data("Pending", 0)
        );
        resultChart.setData(pieData);

        // Culori fixe
        /*pieData.get(0).getNode().setStyle("-fx-pie-color: #4CAF50;");
        pieData.get(1).getNode().setStyle("-fx-pie-color: #F44336;");
        pieData.get(2).getNode().setStyle("-fx-pie-color: #FFC107;");*/

        // Inițializare BarChart
        barSeries = new XYChart.Series<>();
        barSeries.setName("Interview Stats");
        barSeries.getData().add(new XYChart.Data<>("Applications", 0));
        barSeries.getData().add(new XYChart.Data<>("Interviews", 0));
        applicationsChart.getData().add(barSeries);

        NumberAxis yAxis = (NumberAxis) applicationsChart.getYAxis();
        yAxis.setTickUnit(1);
        yAxis.setMinorTickCount(0);
        yAxis.setForceZeroInRange(true);

        // Culori fixe pentru bare
   /*     barSeries.getData().get(0).getNode().setStyle("-fx-bar-fill: blue;");
        barSeries.getData().get(1).getNode().setStyle("-fx-bar-fill: purple;");*/
    }

    public void updateCharts(ObservableList<TableContent> jobs) {
        long accepted = jobs.stream().filter(j -> "Accepted".equals(j.getResult())).count();
        long rejected = jobs.stream().filter(j -> "Unfortunately...".equals(j.getResult())).count();
        long pending = jobs.stream().filter(j -> !"Accepted".equals(j.getResult()) && !"Unfortunately...".equals(j.getResult())).count();

        long totalApps = jobs.size();
        long interviews = jobs.stream().filter(j -> "Yes".equals(j.getInterview())).count();

        pieData.get(0).setPieValue(accepted);
        pieData.get(1).setPieValue(rejected);
        pieData.get(2).setPieValue(pending);

        barSeries.getData().get(0).setYValue(totalApps);
        barSeries.getData().get(1).setYValue(interviews);
    }
}
