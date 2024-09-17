package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;

import javax.sql.DataSource;

import com.example.App;
import com.example.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class ServiceController {

    private Boolean action;

    private Service selectedItem;

    @FXML
    private MainController mainController;

    @FXML
    private Label actionLabel;

    @FXML
    private TableView table;

    @FXML
    private TableColumn idTable;

    @FXML
    private TableColumn categoryTable;

    @FXML
    private TableColumn nameTable;

    @FXML
    private TableColumn costTable;

    @FXML
    private ComboBox categoryFilterComboBox;

    @FXML
    private ComboBox categoryEditComboBox;

    @FXML
    private TextField costEditTextField;

    @FXML
    private TextField nameEditTextField;

    @FXML
    private TextField costFilterTextField;

    @FXML
    private TextField nameFilterTextField;

    ObservableList<Service> data = FXCollections.observableArrayList();

    public void initialize() throws IOException, SQLException {
        ObservableList<String> special = FXCollections.observableArrayList(App.getProperties("special"));
        categoryFilterComboBox.getItems().addAll(special);
        categoryEditComboBox.getItems().addAll(special);

        data.clear();

        idTable.setCellValueFactory(new PropertyValueFactory<>("id"));
        categoryTable.setCellValueFactory(new PropertyValueFactory<>("category"));
        nameTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        costTable.setCellValueFactory(new PropertyValueFactory<>("cost"));

        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        PreparedStatement sqlSelect = conn.prepareStatement("SELECT * FROM service");
        ResultSet rs = sqlSelect.executeQuery();

        while (rs.next()) {
            data.add(new Service(rs.getInt("id"),
            rs.getString("category"),
            rs.getString("name"),
            rs.getDouble("cost")));
            table.setItems(data);
        }
        conn.close();
        rs.close();

    }

    @FXML
    private void selectService() throws IOException {
       selectedItem = (Service) table.getSelectionModel().getSelectedItem();
       if (selectedItem != null) {
        actionLabel.setText("изменение ID = " + selectedItem.getId());
        action = false;
        categoryEditComboBox.getSelectionModel().select(selectedItem.getCategory());;
        costEditTextField.setText(selectedItem.getCost().toString());
        nameEditTextField.setText(selectedItem.getName());
       }
    }

    @FXML
    private void selectActionNewService() {
        action = true;
        actionLabel.setText("новая услуга");
    }

    @FXML
    private void saveServiceData() throws NumberFormatException, SQLException, IOException {
        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        if (action) {
            try {
                PreparedStatement sqlInsert = conn.prepareStatement("INSERT INTO public.service (category, name, cost) VALUES (?, ?, ?)");
                sqlInsert.setString(1, categoryEditComboBox.getSelectionModel().getSelectedItem().toString());
                sqlInsert.setString(2, nameEditTextField.getText());
                sqlInsert.setDouble(3, Double.parseDouble(costEditTextField.getText()));
                sqlInsert.executeUpdate();

                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Новая запись");
                sqlHistory.setString(2, "Услуги");
                sqlHistory.setString(3, "-");
                sqlHistory.setString(4, "-");
                sqlHistory.setString(5, "-");
                sqlHistory.setInt(6, App.getUserId());
                sqlHistory.setObject(7, OffsetDateTime.now());
                sqlHistory.executeUpdate();

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Добавлена новая услуга!");
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
                PreparedStatement sqlUpdate = conn.prepareStatement("UPDATE public.service SET category=?, name=?, cost=? WHERE id=?");
                sqlUpdate.setString(1, categoryEditComboBox.getSelectionModel().getSelectedItem().toString());
                sqlUpdate.setString(2, nameEditTextField.getText());
                sqlUpdate.setDouble(3, Double.parseDouble(costEditTextField.getText()));
                sqlUpdate.setInt(4, selectedItem.getId());

                sqlUpdate.executeUpdate();

                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Изменение");
                sqlHistory.setString(2, "Услуги");
                sqlHistory.setString(3, selectedItem.getId().toString());
                sqlHistory.setString(4, selectedItem.getId().toString());
                sqlHistory.setString(5, selectedItem.getId().toString());
                sqlHistory.setInt(6, App.getUserId());
                sqlHistory.setObject(7, OffsetDateTime.now());

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Изменена услуга с ID = " + selectedItem.getId() + "!");
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
        App.setRoot("service");
    }
    
    @FXML
    private void deleteServiceData() throws SQLException, IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Уведомление");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите удалить ID = " + selectedItem.getId());
        Optional <ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            try {
                DataSource dataSource = App.createDataSource();
                Connection conn = dataSource.getConnection();
                PreparedStatement sqlDelete = conn.prepareStatement("DELETE FROM public.service WHERE id=?");
                sqlDelete.setInt(1, selectedItem.getId());
                sqlDelete.executeUpdate();

                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Удаление");
                sqlHistory.setString(2, "Услуги");
                sqlHistory.setString(3, selectedItem.getId().toString());
                sqlHistory.setString(4, selectedItem.getId().toString());
                sqlHistory.setString(5, "DELETED");
                sqlHistory.setInt(6, App.getUserId());
                sqlHistory.setObject(7, OffsetDateTime.now());

                conn.close();
                Alert alert2 = new Alert(AlertType.INFORMATION);
                alert2.setTitle("Уведомление");
                alert2.setHeaderText(null);
                alert2.setContentText("Удалена услуга с ID = " + selectedItem.getId() + "!");
                alert2.showAndWait();
                App.setRoot("service");
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
    private void filterService() throws NumberFormatException, SQLException, IOException {

        if (!costFilterTextField.getText().isEmpty() || !nameFilterTextField.getText().isEmpty() || categoryFilterComboBox.getSelectionModel().getSelectedItem() != null) {
            String sqlFilterString;

            //Счетчик индекса для preparedstatement
            Integer beginIndex = 1;

            data.clear();

            DataSource dataSource = App.createDataSource();

            Connection conn = dataSource.getConnection();

            PreparedStatement sqlFilter;

            sqlFilterString = "SELECT * FROM public.service WHERE ";

            if (!costFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "cost = ? AND ";
            }
            if (!nameFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "name = ? AND ";
            }
            if (categoryFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilterString+= "category = ? AND ";
            }

            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);

            sqlFilter = conn.prepareStatement(sqlFilterString);

            if (!costFilterTextField.getText().isEmpty()) {
                sqlFilter.setDouble(beginIndex, Double.parseDouble(costFilterTextField.getText()));
                beginIndex+=1;
            }
            if (!nameFilterTextField.getText().isEmpty()) {
                sqlFilter.setString(beginIndex, nameFilterTextField.getText());
                beginIndex+=1;
            }
            if (categoryFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilter.setString(beginIndex, categoryFilterComboBox.getSelectionModel().getSelectedItem().toString());
                beginIndex+=1;
            }
            ResultSet rs = sqlFilter.executeQuery();

            while (rs.next()) {
                data.add(new Service(rs.getInt("id"),
                rs.getString("category"),
                rs.getString("name"),
                rs.getDouble("cost")));
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