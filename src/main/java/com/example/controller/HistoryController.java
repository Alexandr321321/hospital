package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.example.App;
import com.example.History;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistoryController {

    @FXML
    private ComboBox typeComboBox;

    @FXML
    private ComboBox tableComboBox;

    @FXML
    private ComboBox loginComboBox;

    @FXML
    private DatePicker dateDatePicker;

    @FXML
    private TableView table;

    @FXML
    private TableColumn id;

    @FXML
    private TableColumn type;

    @FXML
    private TableColumn tablename;

    @FXML
    private TableColumn field;

    @FXML
    private TableColumn previousvalue;

    @FXML
    private TableColumn newvalue;

    @FXML
    private TableColumn personid;

    @FXML
    private TableColumn datetime;

    ObservableList<History> data = FXCollections.observableArrayList();

    List<String> person = new ArrayList<String>();

    public void initialize() throws SQLException, IOException {
        ObservableList<String> typeList = FXCollections.observableArrayList(App.getProperties("typeOperation"));
        typeComboBox.getItems().addAll(typeList);

        ObservableList<String> tableList = FXCollections.observableArrayList(App.getProperties("tableDB"));
        tableComboBox.getItems().addAll(tableList);

        data = FXCollections.observableArrayList();

        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        PreparedStatement sqlSelectPerson = conn.prepareStatement("SELECT * FROM person");
        ResultSet rsPerson = sqlSelectPerson.executeQuery();

        while (rsPerson.next()) {
            person.add(rsPerson.getString("login"));
        }
        loginComboBox.getItems().addAll(person);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        tablename.setCellValueFactory(new PropertyValueFactory<>("tablename"));
        field.setCellValueFactory(new PropertyValueFactory<>("field"));
        previousvalue.setCellValueFactory(new PropertyValueFactory<>("previousvalue"));
        newvalue.setCellValueFactory(new PropertyValueFactory<>("newvalue"));
        personid.setCellValueFactory(new PropertyValueFactory<>("personid"));
        datetime.setCellValueFactory(new PropertyValueFactory<>("datetime"));

        PreparedStatement sqlSelect = conn.prepareStatement("SELECT history.id, history.type, history.tablename, history.field, history.previousvalue, history.newvalue, history.personid, history.datetime, person.login FROM history JOIN person ON history.personid = person.id");
        ResultSet rs = sqlSelect.executeQuery();

        while (rs.next()) {
            data.add(new History(rs.getInt("id"),
            rs.getString("type"),
            rs.getString("tablename"),
            rs.getString("field"),
            rs.getString("previousvalue"),
            rs.getString("newvalue"),
            rs.getString("personid"),
            rs.getTimestamp("datetime")
            ));
            table.setItems(data);
        }
        conn.close();
        rs.close();
    }

    @FXML
    private void filterHistory() throws NumberFormatException, SQLException, IOException {
        if (typeComboBox.getSelectionModel().getSelectedItem() != null || tableComboBox.getSelectionModel().getSelectedItem() != null || loginComboBox.getSelectionModel().getSelectedItem() != null || dateDatePicker.getValue() != null) {
            String sqlFilterString;

            //Счетчик индекса для preparedstatement
            Integer beginIndex = 1;

            data.clear();

            DataSource dataSource = App.createDataSource();

            Connection conn = dataSource.getConnection();

            PreparedStatement sqlFilter;

            sqlFilterString = "SELECT history.id, history.type, history.tablename, history.field, history.previousvalue, history.newvalue, history.personid, history.datetime, person.login FROM history JOIN person ON history.personid = person.id WHERE ";

            if (typeComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilterString+= "type = ? AND ";
            }
            if (tableComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilterString+= "tablename = ? AND ";
            }
            if (loginComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilterString+= "personid = ? AND ";
            }
            if (dateDatePicker.getValue() != null) {
                sqlFilterString+= "date_trunc('day', datetime) = ? AND ";
            }

            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);

            sqlFilter = conn.prepareStatement(sqlFilterString);

            if (typeComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilter.setString(beginIndex, typeComboBox.getSelectionModel().getSelectedItem().toString());
                beginIndex+=1;
            }
            if (tableComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilter.setString(beginIndex, tableComboBox.getSelectionModel().getSelectedItem().toString());
                beginIndex+=1;
            }
            if (loginComboBox.getSelectionModel().getSelectedItem() != null) {
                PreparedStatement slqGetPersonId = conn.prepareStatement("SELECT id FROM public.person WHERE login=?");
                slqGetPersonId.setString(1, loginComboBox.getSelectionModel().getSelectedItem().toString());
                ResultSet rs = slqGetPersonId.executeQuery();
                while (rs.next()) {
                    sqlFilter.setInt(beginIndex, rs.getInt("id"));
                }
                beginIndex+=1;
            }
            if (dateDatePicker.getValue() != null) {
                sqlFilter.setDate(beginIndex, java.sql.Date.valueOf(dateDatePicker.getValue()));
                beginIndex+=1;
            }
            ResultSet rs = sqlFilter.executeQuery();

            while (rs.next()) {
                data.add(new History(rs.getInt("id"),
                rs.getString("type"),
                rs.getString("tablename"),
                rs.getString("field"),
                rs.getString("previousvalue"),
                rs.getString("newvalue"),
                rs.getString("personid"),
                rs.getTimestamp("datetime")
                ));
                table.setItems(data);
            }
            conn.close();
            rs.close();

        }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Уведомление");
            alert.setHeaderText(null);
            alert.setContentText("Фильтры не заданы!");
            alert.showAndWait();
        }
    }

}
