package com.example.jobapplicationmanager;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;


public class ControllerMainPage {


    @FXML
    private TableView<TableContent> jobDetails;
    @FXML
    private TableColumn<TableContent, String> companyCol;
    @FXML
    private TableColumn<TableContent, String> titleCol;
    @FXML
    private TableColumn<TableContent, LocalDate> appDateCol;
    @FXML
    private TableColumn<TableContent, LocalDate> testDateCol;
    @FXML
    private TableColumn<TableContent, String> interviewCol;
    @FXML
    private TableColumn<TableContent, String> accountCol;
    @FXML
    private TableColumn<TableContent, String> linkCol;
    @FXML
    private TableColumn<TableContent, String> resultCol;
    @FXML
    private PieChart resultChart;
    @FXML
    private BarChart<String, Number> applicationsChart;
    @FXML
    public Label helpIcon;
    private ChartService chartService;


    @FXML
    public void initialize() {
        DatabaseHelper.initializeDatabase();
        columnConnections();
        deleteRows();
        infoTip();


        // Populez tabelul din DB
        jobDetails.setItems(JobDao.getAllJobs());
        jobDetails.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Editarea randurilor
        setupTextColumn(companyCol, TableContent::setCompanyName);
        setupTextColumn(titleCol, TableContent::setJobTitle);
        setupTextColumn(accountCol, TableContent::setAccountInfo);
        setupTextColumn(linkCol, TableContent::setLink);

        ObservableList<String> optionsResult = FXCollections.observableArrayList("Pending", "Unfortunately...", "Accepted");
        ObservableList<String> optionsInterview = FXCollections.observableArrayList("Yes", "No");
        setupComboColumn(resultCol, optionsResult, TableContent::setResult);
        setupComboColumn(interviewCol, optionsInterview, TableContent::setInterview);


        // Inițializez ChartService
        chartService = new ChartService(resultChart, applicationsChart);
        chartService.updateCharts(jobDetails.getItems());

        // Actualizare automată la modificări
        jobDetails.getItems().addListener((ListChangeListener<TableContent>) change ->
                chartService.updateCharts(jobDetails.getItems()));

    }
    public void infoTip(){
        Tooltip tooltip = new Tooltip(
                "Instructions:\n" +
                        "• To add a row u have to enter the name of the company on your last row.\n" +
                        "• To delete a row, u have to right click it and then select: Delete Row."
        );

        // elimin delay-ul default de ~1s
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setHideDelay(Duration.ZERO);

        helpIcon.setTooltip(tooltip);
        helpIcon.setFocusTraversable(true); // <--- asta e cheia, altfel uneori nu merge

    }

    private void deleteRows() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete Row");
        deleteItem.setOnAction(event -> {
            TableContent selectedItem = jobDetails.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                jobDetails.getItems().remove(selectedItem);
                //JobDao.delete(selectedItem); // ștergere din DB
                if (jobDetails.getItems().isEmpty()) {
                    jobDetails.getItems().add(new TableContent(
                            "Google", "Intern", LocalDate.now(), null, "Yes", "Pending", "myemail@gmail.com", "https://jobs.com"
                    ));
                }
                //updatePieChart();
                // updateBarChart();
                chartService.updateCharts(getJobs());
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
           /* updateBarChart();
            updatePieChart();*/
            chartService.updateCharts(getJobs());

        });
    }

    private void columnConnections() {
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


    public ObservableList<TableContent> getJobs() {
        return jobDetails.getItems();
    }


}
