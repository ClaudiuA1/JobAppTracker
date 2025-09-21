module com.example.jobapplicationmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.example.jobapplicationmanager to javafx.fxml;
    exports com.example.jobapplicationmanager;
}