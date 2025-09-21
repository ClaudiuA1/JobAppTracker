package com.example.jobapplicationmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    private HelloApplication helloApplication=new HelloApplication();

    @FXML
    protected void onHelloButtonClick() throws IOException {

        welcomeText.setText("Welcome to JavaFX Application!");


    }


}
