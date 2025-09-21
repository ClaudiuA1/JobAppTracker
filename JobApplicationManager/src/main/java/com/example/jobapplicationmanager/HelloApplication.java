package com.example.jobapplicationmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Image img =new Image(Objects.requireNonNull(getClass().getResourceAsStream("JobAppLogo.png")));
        scene.getStylesheets().add(getClass().getResource("mainPage.css").toExternalForm());

        ControllerMainPage controller =fxmlLoader.getController();

        // La închidere salvez tot tabelul
        stage.setOnCloseRequest(event -> {
            JobDao.saveAll(controller.getJobs());
        });

        stage.getIcons().add(img);
        stage.setTitle("Job Applications Tracker");
        stage.setScene(scene);
        stage.show();
    }

}
