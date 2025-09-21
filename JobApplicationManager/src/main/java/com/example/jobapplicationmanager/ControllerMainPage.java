package com.example.jobapplicationmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;

import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;


public class ControllerMainPage {

    @FXML private  TableView<TableContent> jobDetails;
    @FXML private TableColumn<TableContent, String> companyCol;
    @FXML private TableColumn<TableContent, String> titleCol;
    @FXML private TableColumn<TableContent, LocalDate> appDateCol;
    @FXML private TableColumn<TableContent, LocalDate> testDateCol;
    @FXML private TableColumn<TableContent, String> interviewCol;
    @FXML private TableColumn<TableContent, String> accountCol;
    @FXML private TableColumn<TableContent, String> linkCol;
    @FXML private TableColumn<TableContent, String> resultCol;
    @FXML
    private PieChart resultChart;
    @FXML
    private BarChart<String, Number> applicationsChart;


    @FXML
    public void initialize() {
       DatabaseHelper.initializeDatabase();
       columnConnections();
       updatePieChart();
       updateBarChart();
        // Populez tabelul din DB
        jobDetails.setItems(JobDao.getAllJobs());

        // Adaug un exemplu de rând
        jobDetails.getItems().add(new TableContent(
               "Google", "Intern", LocalDate.now(), null, "Yes", "Pending", "myemail@gmail.com", "https://jobs.com"
        ));
        jobDetails.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


// Editarea randurilor
        setupTextColumn(companyCol, TableContent::setCompanyName);
        setupTextColumn(titleCol, TableContent::setJobTitle);
        setupTextColumn(accountCol, TableContent::setAccountInfo);
        setupTextColumn(linkCol, TableContent::setLink);

        ObservableList<String> optionsResult = FXCollections.observableArrayList("Pending", "Unfortunately...", "Accepted");
        ObservableList<String> optionsInterview = FXCollections.observableArrayList("Yes","No");
        setupComboColumn(resultCol,optionsResult, TableContent::setResult);
        setupComboColumn(interviewCol,optionsInterview, TableContent::setInterview);
//
        chartsPopulation();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete Row");
        deleteItem.setOnAction(event -> {
            TableContent selectedItem = jobDetails.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                jobDetails.getItems().remove(selectedItem);
                //JobDao.delete(selectedItem); // ștergere din DB
                 if(jobDetails.getItems() == null){
                            jobDetails.getItems().add(new TableContent());

                        }
                updatePieChart();
                updateBarChart();
            }
        });
        contextMenu.getItems().add(deleteItem);


        jobDetails.setContextMenu(contextMenu);



    }

    private void addRowIfLast(int editedRowIndex) {
        if (editedRowIndex == jobDetails.getItems().size() - 1) {
            jobDetails.getItems().add(new TableContent());
        }

    }
    private void setupTextColumn(
            TableColumn<TableContent, String> col,
            BiConsumer<TableContent, String> setter) {

        col.setCellFactory(TextFieldTableCell.forTableColumn());
        col.setOnEditCommit(event -> {
            TableContent item = event.getRowValue();
            setter.accept(item, event.getNewValue()); // aplică setterul pe obiect
            addRowIfLast(event.getTablePosition().getRow());


        });
    }
    private void setupComboColumn(
            TableColumn<TableContent, String> col,
            ObservableList<String> options,
            BiConsumer<TableContent, String> setter) {

        col.setCellFactory(ComboBoxTableCell.forTableColumn(options));
        col.setOnEditCommit(event -> {
            TableContent item = event.getRowValue();
            setter.accept(item, event.getNewValue());
          //  addRowIfLast(event.getTablePosition().getRow());
            updateBarChart();
            updatePieChart();
        });
    }

    private void columnConnections(){
        jobDetails.setEditable(true);
        // Col normale
        companyCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        interviewCol.setCellValueFactory(new PropertyValueFactory<>("interview"));
        accountCol.setCellValueFactory(new PropertyValueFactory<>("accountInfo"));
        linkCol.setCellValueFactory(new PropertyValueFactory<>("link"));

        // Col cu DatePicker
        appDateCol.setCellValueFactory(new PropertyValueFactory<>("applicationDate"));
        appDateCol.setCellFactory(col -> new DatePickerTableCell<>());

        testDateCol.setCellValueFactory(new PropertyValueFactory<>("technicalTestDate"));
        testDateCol.setCellFactory(col -> new DatePickerTableCell<>());

        // Col cu ComboBox
        resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
        resultCol.setCellFactory(ComboBoxTableCell.forTableColumn("Pending", "Accepted", "Unfortunately..."));
    }

    public void chartsPopulation(){
        long accepted = jobDetails.getItems().stream()
                .filter(item -> "Accepted".equals(item.getResult()))
                .count();

        long rejected = jobDetails.getItems().stream()
                .filter(item -> "Unfortunately...".equals(item.getResult()))
                .count();

        long pending = jobDetails.getItems().stream()
                .filter(item -> "Pending".equals(item.getResult()))
                .count();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Accepted", accepted),
                new PieChart.Data("Rejected", rejected),
                new PieChart.Data("Pending", pending)
        );
        resultChart.setData(pieData);

// aplicări = total rânduri
        long totalApplications = jobDetails.getItems().size();

// interviuri = "Yes"
        long interviews = jobDetails.getItems().stream()
                .filter(item -> "Yes".equals(item.getInterview()))
                .count();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Statistici");
        series.getData().add(new XYChart.Data<>("Aplicări", totalApplications));
        series.getData().add(new XYChart.Data<>("Interviuri", interviews));

        NumberAxis yAxis = (NumberAxis) applicationsChart.getYAxis();
        yAxis.setTickUnit(1);        // Pasul între valori
        yAxis.setMinorTickCount(0);  // Fără subdiviziuni
        yAxis.setForceZeroInRange(true); // Include zero


        applicationsChart.getData().add(series);

    }
    public void updateBarChart() {
        long totalApps = jobDetails.getItems().size();
        long interviews = jobDetails.getItems().stream()
                .filter(item -> "Yes".equals(item.getInterview()))
                .count();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Applications", totalApps));
        series.getData().add(new XYChart.Data<>("Interviews", interviews));

        applicationsChart.getData().setAll(series); // șterge și pune seria nouă
    }
    public void updatePieChart() {
        long accepted = jobDetails.getItems().stream().filter(item -> "Accepted".equals(item.getResult())).count();
        long rejected = jobDetails.getItems().stream().filter(item -> "Unfortunately...".equals(item.getResult())).count();
        long pending = jobDetails.getItems().stream().filter(item -> !"Accepted".equals(item.getResult()) && !"Unfortunately...".equals(item.getResult())).count();

        resultChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Accepted", accepted),
                new PieChart.Data("Rejected", rejected),
                new PieChart.Data("Pending", pending)
        ));
    }
    public ObservableList<TableContent> getJobs() {
        return jobDetails.getItems();
    }





}
