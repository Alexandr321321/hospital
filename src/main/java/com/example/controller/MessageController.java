package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import com.example.App;
import com.example.Job;
import com.example.Service;
import com.example.Message;
import com.example.Person;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class MessageController {

    ObservableList<Message> data;

    private Message selectedItem;

    private Boolean action;

    @FXML
    private ComboBox personFilterComboBox;

    @FXML
    private DatePicker dateFilterDatePicker;

    @FXML
    private Label actionLabel;

    @FXML
    private TableView table;

    @FXML
    private TableColumn idTable;

    @FXML
    private TableColumn personTable;

    @FXML
    private TableColumn messageTable;

    @FXML
    private TableColumn dateTable;

    @FXML
    private TextArea message;

    List<String> person = new ArrayList<String>();

    public void initialize() throws IOException, SQLException {
        data = FXCollections.observableArrayList();

        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();
        PreparedStatement sqlSelectPerson = conn.prepareStatement("SELECT * FROM person");
        ResultSet rsPerson = sqlSelectPerson.executeQuery();


        while (rsPerson.next()) {
            person.add(rsPerson.getString("login"));
        }
        personFilterComboBox.getItems().addAll(person);

        data.clear();

        idTable.setCellValueFactory(new PropertyValueFactory<>("id"));
        personTable.setCellValueFactory(new PropertyValueFactory<>("personid"));
        messageTable.setCellValueFactory(new PropertyValueFactory<>("text"));
        dateTable.setCellValueFactory(new PropertyValueFactory<>("date"));

        PreparedStatement sqlSelect = conn.prepareStatement("SELECT comment.id, comment.personid, comment.text, comment.date, person.login FROM comment JOIN person ON comment.personid = person.id");
        ResultSet rs = sqlSelect.executeQuery();
        while (rs.next()) {
            data.add(new Message(rs.getInt("id"),
            rs.getString("login"),
            rs.getString("text"),
            rs.getDate("date")));
            table.setItems(data);
        }
        conn.close();
        rs.close();
    }

    @FXML
    private void selectMessage() throws IOException {
       selectedItem = (Message) table.getSelectionModel().getSelectedItem();
       if (selectedItem != null) {
        actionLabel.setText("изменение ID = " + selectedItem.getId());
        action = false;
        message.setText(selectedItem.getText());
       }
    }

    @FXML
    private void selectActionNewMessage() {
        action = true;
        actionLabel.setText("новая запись");
    }

    @FXML
    private void saveMessageData() throws NumberFormatException, SQLException, IOException {
        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        if (action) {
            try {
                
                PreparedStatement sqlInsert = conn.prepareStatement("INSERT INTO public.comment (personid, text, date) VALUES (?, ?, ?)");
                sqlInsert.setInt(1, App.getUserId());
                sqlInsert.setString(2, message.getText());
                sqlInsert.setDate(3, Date.valueOf(LocalDate.now()));
                sqlInsert.executeUpdate();

                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Новая запись");
                sqlHistory.setString(2, "Сообщения");
                sqlHistory.setString(3, "-");
                sqlHistory.setString(4, "-");
                sqlHistory.setString(5, "-");
                sqlHistory.setInt(6, App.getUserId());
                sqlHistory.setObject(7, OffsetDateTime.now());
                sqlHistory.executeUpdate();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Добавлена новая запись!");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Проверьте заполненость полей! Ошибка: " + e);
                alert.showAndWait();
            }
        }
        if (!action) {
            try {
                PreparedStatement sqlInsert = conn.prepareStatement("UPDATE public.comment SET personid=?, text=?, date=? WHERE id=?");
                PreparedStatement slqGetPersonId = conn.prepareStatement("SELECT id FROM public.person WHERE login=?");
                slqGetPersonId.setString(1, selectedItem.getPersonid());
                ResultSet rs = slqGetPersonId.executeQuery();
                while (rs.next()) {
                    sqlInsert.setString(1, rs.getString("id"));
                }
                sqlInsert.setString(2, message.getText());
                sqlInsert.setDate(3, Date.valueOf(LocalDate.now()));
                sqlInsert.setInt(4, selectedItem.getId());
                sqlInsert.executeUpdate();

                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Изменение");
                sqlHistory.setString(2, "Сообщения");
                sqlHistory.setString(3, selectedItem.getId().toString());
                sqlHistory.setString(4, selectedItem.getId().toString());
                sqlHistory.setString(5, selectedItem.getId().toString());
                sqlHistory.setInt(6, App.getUserId());
                sqlHistory.setObject(7, OffsetDateTime.now());
                sqlHistory.executeUpdate();

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Запись изменена!");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Проверьте заполненость полей! Ошибка: " + e);
                alert.showAndWait();
            }
        }  
        conn.close();
        App.setRoot("message");
    }

    @FXML
    private void deleteMessageData() throws SQLException, IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Уведомление");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите удалить ID = " + selectedItem.getId());
        Optional <ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            try {
                DataSource dataSource = App.createDataSource();
                Connection conn = dataSource.getConnection();
                PreparedStatement sqlDelete = conn.prepareStatement("DELETE FROM public.comment WHERE id=?");
                sqlDelete.setInt(1, selectedItem.getId());
                sqlDelete.executeUpdate();

                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Удаление");
                sqlHistory.setString(2, "Сообщения");
                sqlHistory.setString(3, selectedItem.getId().toString());
                sqlHistory.setString(4, selectedItem.getId().toString());
                sqlHistory.setString(5, "DELETED");
                sqlHistory.setInt(6, App.getUserId());
                sqlHistory.setObject(7, OffsetDateTime.now());
                sqlHistory.executeUpdate();
                conn.close();
                Alert alert2 = new Alert(AlertType.INFORMATION);
                alert2.setTitle("Уведомление");
                alert2.setHeaderText(null);
                alert2.setContentText("Удалено сообщение с ID = " + selectedItem.getId() + "!");
                alert2.showAndWait();
                App.setRoot("message");
            } catch (Exception e) {
                Alert alert3 = new Alert(AlertType.ERROR);
                alert3.setTitle("Уведомление");
                alert3.setHeaderText(null);
                alert3.setContentText("Ошибка при удалении! " + e);
                alert3.showAndWait();
            }
        }
    }

    @FXML
    private void filterMessage() throws NumberFormatException, SQLException, IOException {

        if (personFilterComboBox.getSelectionModel().getSelectedItem() != null || dateFilterDatePicker.getValue() != null) {
            
            String sqlFilterString;

            //Счетчик индекса для preparedstatement
            Integer beginIndex = 1;

            data.clear();

            DataSource dataSource = App.createDataSource();

            Connection conn = dataSource.getConnection();

            PreparedStatement sqlFilter;

            sqlFilterString = "SELECT * FROM public.message WHERE ";

            if (personFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilterString+= "personid = ? AND ";
            }
            if (dateFilterDatePicker.getValue() != null) {
                sqlFilterString+= "date = ? AND ";
            }

            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);

            sqlFilter = conn.prepareStatement(sqlFilterString);

            if (personFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                PreparedStatement slqGetPersonId = conn.prepareStatement("SELECT id FROM public.person WHERE login=?");
                slqGetPersonId.setString(1, personFilterComboBox.getSelectionModel().getSelectedItem().toString());
                ResultSet rs = slqGetPersonId.executeQuery();
                while (rs.next()) {
                    sqlFilter.setInt(1, rs.getInt("id"));
                }
                beginIndex+=1;
            }
            if (dateFilterDatePicker.getValue() != null) {
                sqlFilter.setDate(beginIndex, java.sql.Date.valueOf(dateFilterDatePicker.getValue()));
                beginIndex+=1;
            }
            ResultSet rs = sqlFilter.executeQuery();

            while (rs.next()) {
                data.add(new Message(rs.getInt("id"),
            rs.getString("login"),
            rs.getString("text"),
            rs.getDate("date")));
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