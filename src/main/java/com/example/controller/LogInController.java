package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.example.App;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogInController {

    @FXML
    private TextField login;
    @FXML
    private PasswordField password;

    public void initialize() {
        login.setText("sovetnikovas");
        password.setText("w123456789");
    }

    @FXML
    private void logIn() throws IOException, SQLException {
        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        PreparedStatement sqlSelect = conn.prepareStatement("SELECT id, login, password FROM person WHERE login=? AND password=?");
        sqlSelect.setString(1, login.getText());
        sqlSelect.setString(2, password.getText());

        ResultSet rs = sqlSelect.executeQuery();
        /* 
        while (rs.next()) {
            PreparedStatement sqlUpdate = conn.prepareStatement("UPDATE public.person SET active=True WHERE login=?");
            sqlUpdate.setString(1, login.getText());
            sqlUpdate.executeUpdate();
            App.setRoot("massage");
        } */

        if (rs.next() == true) {
            

            PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
            sqlHistory.setString(1, "Вход");
            sqlHistory.setString(2, "-");
            sqlHistory.setString(3, "-");
            sqlHistory.setString(4, "-");
            sqlHistory.setString(5, "-");
            sqlHistory.setInt(6, rs.getInt("id"));
            sqlHistory.setObject(7, OffsetDateTime.now());
            sqlHistory.executeUpdate();
            App.setUserId(rs.getInt("id"));

            App.setRoot("message");

            rs.close();
            conn.close();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Уведомление");
            alert.setHeaderText(null);
            alert.setContentText("Пользователь с таким логином и паролем не найден!");
            alert.showAndWait();
            rs.close();
            conn.close();
        }
    }

}
