package com.example.controller;

import java.io.IOException;

import com.example.App;

import javafx.fxml.FXML;

public class MainController {

    @FXML
    private void switchToMessage() throws IOException {
        App.setRoot("message");
    }

    @FXML
    private void switchToContract() throws IOException {
        App.setRoot("contract");
    }

    @FXML
    private void switchToJob() throws IOException {
        App.setRoot("job");
    }

    @FXML
    private void switchToCard() throws IOException {
        App.setRoot("card");
    }

    @FXML
    private void switchToKass() throws IOException {
        App.setRoot("kass");
    }

    @FXML
    private void switchToPerson() throws IOException {
        App.setRoot("person");
    }
    
    @FXML
    private void switchToLogIn() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void switchToHistory() throws IOException {
        App.setRoot("history");
    }

    @FXML
    private void switchToService() throws IOException {
        App.setRoot("service");
    }

}