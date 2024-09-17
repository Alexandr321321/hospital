package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.example.App;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;

public class KassController {

    @FXML
    private DatePicker date;

    private String sumOnDay = "0";

    private String sumCashOnDay = "0";

    private String sumNonCashOnDay = "0";

    @FXML
    private void generateReport() throws IOException, SQLException {

        /* try { */
            DataSource dataSource = App.createDataSource();

            Connection conn = dataSource.getConnection();

            PreparedStatement sqlGetSumOnDay = conn.prepareStatement("SELECT COALESCE(SUM(sum), 0) AS sum  FROM public.contract WHERE date_trunc('day', lower(datetimerange)) = ?");
            sqlGetSumOnDay.setDate(1, java.sql.Date.valueOf(date.getValue()));
            ResultSet rsGetSumOnDay = sqlGetSumOnDay.executeQuery();

            while (rsGetSumOnDay.next()) {
                sumOnDay = String.valueOf(rsGetSumOnDay.getDouble("sum"));
            }

            PreparedStatement sqlGetSumCashOnDay = conn.prepareStatement("SELECT COALESCE(SUM(sum), 0) AS sum FROM public.contract WHERE date_trunc('day', lower(datetimerange)) = ? AND paytype = 'Наличный'");
            sqlGetSumCashOnDay.setDate(1, java.sql.Date.valueOf(date.getValue()));
            ResultSet rsGetSumCashOnDay = sqlGetSumCashOnDay.executeQuery();

            while (rsGetSumCashOnDay.next()) {
                sumCashOnDay = String.valueOf(rsGetSumCashOnDay.getDouble("sum"));
            }

            PreparedStatement sqlGetSumNonCashOnDay = conn.prepareStatement("SELECT COALESCE(SUM(sum), 0) AS sum  FROM public.contract WHERE date_trunc('day', lower(datetimerange)) = ? AND paytype = 'Безналичный'");
            sqlGetSumNonCashOnDay.setDate(1, java.sql.Date.valueOf(date.getValue()));
            ResultSet rsGetSumNonCashOnDay = sqlGetSumNonCashOnDay.executeQuery();

            while (rsGetSumNonCashOnDay.next()) {
                sumNonCashOnDay = String.valueOf(rsGetSumNonCashOnDay.getDouble("sum"));
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Отчёт");
            alert.setHeaderText(null);
            alert.setContentText("Отчёт на дату " + date.getValue() + "\nИтого: " + sumOnDay + "\nНаличный расчёт: " + sumCashOnDay + "\nБезналичный расчёт: " + sumNonCashOnDay);
            alert.showAndWait();

        /* } catch (Exception e) {
            // TODO: handle exception
        } */

    }

}
