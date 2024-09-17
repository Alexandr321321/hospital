package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import javax.sql.DataSource;

import java.util.Optional;

import com.example.App;
import com.example.Person;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class PersonController {
    
    private Person selectedItem;

    private Boolean action;

    @FXML
    private Label actionLabel;

    @FXML
    private MainController mainController;

    @FXML
    private TextField secondNameFilterTextField;

    @FXML
    private TextField loginFilterTextField;

    @FXML
    private ComboBox statusFilterComboBox;

    @FXML
    private ComboBox roleFilterComboBox;

    @FXML
    private ComboBox specialFilterComboBox;



    @FXML
    private TableView table;

    @FXML
    private TableColumn idTable;

    @FXML
    private TableColumn loginTable;

    @FXML
    private TableColumn secondnameTable;

    @FXML
    private TableColumn statusTable;

    @FXML
    private TableColumn roleTable;

    @FXML
    private TableColumn specialTable;



    @FXML
    private TextField loginEditTextField;

    @FXML
    private PasswordField passwordEditTextField;

    @FXML
    private ComboBox roleEditComboBox;

    @FXML
    private ComboBox specialEditComboBox;

    @FXML
    private TextField secondNameEditTextField;

    @FXML
    private TextField firstNameEditTextField;

    @FXML
    private TextField familyEditTextField;

    @FXML
    private CheckBox statusEditCheckBox;



    @FXML
    private TextField phoneEditTextField;

    @FXML
    private TextField emailEditTextField;

    @FXML
    private DatePicker birthDayEditDatePicker;

    @FXML
    private RadioButton genderManEditRadioButton;

    @FXML
    private RadioButton genderWomanEditRadioButton;

    @FXML
    private TextField birthdayPlaceEditTextField;

    

    @FXML
    private TextField passSeriesEditTextField;

    @FXML
    private TextField passNumEditTextField;

    @FXML
    private DatePicker passDateIssueEditDatePicker;

    @FXML
    private TextField passDepartmentCodeEditTextField;

    @FXML
    private TextField passIssuePlaceEditTextField;



    @FXML
    private TextField innEditTextField;

    @FXML
    private TextField snilsEditTextField;



    ObservableList<Person> data;

    public void initialize() throws SQLException, IOException {


        ObservableList<String> statusList = FXCollections.observableArrayList("true", "false");
        statusFilterComboBox.getItems().addAll(statusList);

        ObservableList<String> roleList = FXCollections.observableArrayList(App.getProperties("role"));
        roleFilterComboBox.getItems().addAll(roleList);
        roleEditComboBox.getItems().addAll(roleList);

        ObservableList<String> specialList = FXCollections.observableArrayList(App.getProperties("specialist"));
        specialFilterComboBox.getItems().addAll(specialList);
        specialEditComboBox.getItems().addAll(specialList);
        
        data = FXCollections.observableArrayList();

        idTable.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginTable.setCellValueFactory(new PropertyValueFactory<>("login"));
        secondnameTable.setCellValueFactory(new PropertyValueFactory<>("secondname"));
        statusTable.setCellValueFactory(new PropertyValueFactory<>("status"));
        roleTable.setCellValueFactory(new PropertyValueFactory<>("role"));
        specialTable.setCellValueFactory(new PropertyValueFactory<>("special"));

        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        PreparedStatement sqlSelect = conn.prepareStatement("SELECT * FROM person");
        ResultSet rs = sqlSelect.executeQuery();

        while (rs.next()) {
            data.add(new Person(rs.getInt("id"),
            rs.getString("login"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getDate("birthday"),
            rs.getString("birthdayplace"),
            rs.getString("gender"),
            rs.getString("role"),
            rs.getString("special"),
            rs.getLong("inn"),
            rs.getLong("snils"),
            rs.getBoolean("status"),
            rs.getString("secondname"),
            rs.getString("name"),
            rs.getString("family"),
            rs.getString("phone"),
            rs.getLong("passseries"),
            rs.getLong("passnum"),
            rs.getDate("passdateissue"),
            rs.getString("passissueplace"),
            rs.getInt("passdepartmentcode"))); 
            table.setItems(data);
        }
        conn.close();
        rs.close();
    }

    @FXML
    private void selectPerson() throws IOException {
       selectedItem = (Person) table.getSelectionModel().getSelectedItem();
       if (selectedItem != null) {
        actionLabel.setText("изменение ID = " + selectedItem.getId());
        action = false;
        loginEditTextField.setText(selectedItem.getLogin());
        passwordEditTextField.setText(selectedItem.getPassword());
        roleEditComboBox.getSelectionModel().select(selectedItem.getRole());
        specialEditComboBox.getSelectionModel().select(selectedItem.getSpecial());
        statusEditCheckBox.setSelected(selectedItem.getStatus());
        secondNameEditTextField.setText(selectedItem.getSecondname());
        firstNameEditTextField.setText(selectedItem.getName());
        familyEditTextField.setText(selectedItem.getFamily());
        phoneEditTextField.setText(selectedItem.getPhone());
        emailEditTextField.setText(selectedItem.getEmail());
        birthDayEditDatePicker.setValue(App.convertToLocalDateViaSqlDate(selectedItem.getBirthday()));
        if (selectedItem.getGender().equals("Мужской")) {
            genderManEditRadioButton.setSelected(true);
        }
        if (selectedItem.getGender().equals("Женский")){
            genderWomanEditRadioButton.setSelected(true);
        }
        birthdayPlaceEditTextField.setText(selectedItem.getBirthdayPlace());
        passSeriesEditTextField.setText(selectedItem.getPassseries().toString());
        passNumEditTextField.setText(selectedItem.getPassnum().toString());
        passDateIssueEditDatePicker.setValue(App.convertToLocalDateViaSqlDate(selectedItem.getPassdateissue()));
        passDepartmentCodeEditTextField.setText(selectedItem.getPassdepartmentcode().toString());
        passIssuePlaceEditTextField.setText(selectedItem.getPassissueplace());
        innEditTextField.setText(selectedItem.getInn().toString());
        snilsEditTextField.setText(selectedItem.getSnils().toString());
       }
    }

    @FXML
    private void selectActionNewPerson() {
        action = true;
        actionLabel.setText("новый сотрудник");
    }

    @FXML
    private void savePersonData() throws NumberFormatException, SQLException, IOException {
        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        if (action) {
            try {
                PreparedStatement sqlInsert = conn.prepareStatement("INSERT INTO public.person (login, password, email, birthday, gender, role, inn, snils, status, special, birthdayplace, secondname, name, family, passseries, passnum, passdateissue, passissueplace, passdepartmentcode, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                sqlInsert.setString(1, loginEditTextField.getText());
                sqlInsert.setString(2, passwordEditTextField.getText());
                sqlInsert.setString(3, emailEditTextField.getText());
                sqlInsert.setDate(4, java.sql.Date.valueOf(birthDayEditDatePicker.getValue()));
                if (genderManEditRadioButton.isSelected()) {
                    sqlInsert.setString(5, "Мужской");
                }
                if (genderWomanEditRadioButton.isSelected()) {
                    sqlInsert.setString(5, "Женский");
                }
                sqlInsert.setString(6, roleEditComboBox.getSelectionModel().getSelectedItem().toString());
                sqlInsert.setLong(7, Long.parseLong(innEditTextField.getText()));
                sqlInsert.setLong(8, Long.parseLong(snilsEditTextField.getText()));
                sqlInsert.setBoolean(9, statusEditCheckBox.isSelected());
                sqlInsert.setString(10, specialEditComboBox.getSelectionModel().getSelectedItem().toString());
                sqlInsert.setString(11, birthdayPlaceEditTextField.getText());
                sqlInsert.setString(12, secondNameEditTextField.getText());
                sqlInsert.setString(13, firstNameEditTextField.getText());
                sqlInsert.setString(14, familyEditTextField.getText());
                sqlInsert.setLong(15, Long.parseLong(passSeriesEditTextField.getText()));
                sqlInsert.setLong(16, Long.parseLong(passNumEditTextField.getText()));
                sqlInsert.setDate(17, java.sql.Date.valueOf(passDateIssueEditDatePicker.getValue()));
                sqlInsert.setString(18, passIssuePlaceEditTextField.getText());
                sqlInsert.setLong(19, Long.parseLong(passDepartmentCodeEditTextField.getText()));
                sqlInsert.setString(20, phoneEditTextField.getText());
                sqlInsert.executeUpdate();
                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Новая запись");
                sqlHistory.setString(2, "Сотрудники");
                sqlHistory.setString(3, "-");
                sqlHistory.setString(4, "-");
                sqlHistory.setString(5, "-");
                sqlHistory.setInt(6, App.getUserId());
                sqlHistory.setObject(7, OffsetDateTime.now());
                sqlHistory.executeUpdate();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Добавлен новый пользователь!");
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
                PreparedStatement sqlUpdate = conn.prepareStatement("UPDATE public.person SET login=?, password=?, email=?, birthday=?, gender=?, role=?, inn=?, snils=?, status=?, special=?, birthdayplace=?, secondname=?, name=?, family=?, passseries=?, passnum=?, passdateissue=?, passissueplace=?, passdepartmentcode=?, phone=? WHERE id=?;");
                sqlUpdate.setString(1, loginEditTextField.getText());
                sqlUpdate.setString(2, passwordEditTextField.getText());
                sqlUpdate.setString(3, emailEditTextField.getText());
                sqlUpdate.setDate(4, java.sql.Date.valueOf(birthDayEditDatePicker.getValue()));
                if (genderManEditRadioButton.isSelected()) {
                    sqlUpdate.setString(5, "Мужской");
                }
                if (genderWomanEditRadioButton.isSelected()) {
                    sqlUpdate.setString(5, "Женский");
                }
                sqlUpdate.setString(6, roleEditComboBox.getSelectionModel().getSelectedItem().toString());
                sqlUpdate.setLong(7, Long.parseLong(innEditTextField.getText()));
                sqlUpdate.setLong(8, Long.parseLong(snilsEditTextField.getText()));
                sqlUpdate.setBoolean(9, statusEditCheckBox.isSelected());
                sqlUpdate.setString(10, specialEditComboBox.getSelectionModel().getSelectedItem().toString());
                sqlUpdate.setString(11, birthdayPlaceEditTextField.getText());
                sqlUpdate.setString(12, secondNameEditTextField.getText());
                sqlUpdate.setString(13, firstNameEditTextField.getText());
                sqlUpdate.setString(14, familyEditTextField.getText());
                sqlUpdate.setLong(15, Long.parseLong(passSeriesEditTextField.getText()));
                sqlUpdate.setLong(16, Long.parseLong(passNumEditTextField.getText()));
                sqlUpdate.setDate(17, java.sql.Date.valueOf(passDateIssueEditDatePicker.getValue()));
                sqlUpdate.setString(18, passIssuePlaceEditTextField.getText());
                sqlUpdate.setLong(19, Long.parseLong(passDepartmentCodeEditTextField.getText()));
                sqlUpdate.setString(20, phoneEditTextField.getText());
                sqlUpdate.setInt(21, selectedItem.getId());

                sqlUpdate.executeUpdate();

                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Изменение");
                sqlHistory.setString(2, "Сотрудники");
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
        App.setRoot("person");
    }

    @FXML
    private void deletePersonData() throws SQLException, IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Уведомление");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите удалить ID = " + selectedItem.getId());
        Optional <ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            try {
                DataSource dataSource = App.createDataSource();
                Connection conn = dataSource.getConnection();
                PreparedStatement sqlDelete = conn.prepareStatement("DELETE FROM public.person WHERE id=?");
                sqlDelete.setInt(1, selectedItem.getId());
                sqlDelete.executeUpdate();
                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Удаление");
                sqlHistory.setString(2, "Сотрудники");
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
                alert2.setContentText("Удалена пользователь с ID = " + selectedItem.getId() + "!");
                alert2.showAndWait();
                App.setRoot("person");
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
    private void filterPerson() throws NumberFormatException, SQLException, IOException {

        if (!secondNameFilterTextField.getText().isEmpty() || !loginFilterTextField.getText().isEmpty() || statusFilterComboBox.getSelectionModel().getSelectedItem() != null || roleFilterComboBox.getSelectionModel().getSelectedItem() != null || specialFilterComboBox.getSelectionModel().getSelectedItem() != null) {
            
            String sqlFilterString;

            //Счетчик индекса для preparedstatement
            Integer beginIndex = 1;

            data.clear();

            DataSource dataSource = App.createDataSource();

            Connection conn = dataSource.getConnection();

            PreparedStatement sqlFilter;

            sqlFilterString = "SELECT * FROM public.person WHERE ";

            if (!secondNameFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "secondname = ? AND ";
            }
            if (!loginFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "login = ? AND ";
            }
            if (statusFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilterString+= "status = ? AND ";
            }
            if (roleFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilterString+= "role = ? AND ";
            }
            if (specialFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilterString+= "special = ? AND ";
            }

            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);

            sqlFilter = conn.prepareStatement(sqlFilterString);

            if (!secondNameFilterTextField.getText().isEmpty()) {
                sqlFilter.setString(beginIndex, secondNameFilterTextField.getText());
                beginIndex+=1;
            }
            if (!loginFilterTextField.getText().isEmpty()) {
                sqlFilter.setString(beginIndex, loginFilterTextField.getText());
                beginIndex+=1;
            }
            if (statusFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilter.setBoolean(beginIndex, Boolean.valueOf(statusFilterComboBox.getSelectionModel().getSelectedItem().toString()));
                beginIndex+=1;
            }
            if (roleFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilter.setString(beginIndex, roleFilterComboBox.getSelectionModel().getSelectedItem().toString());
                beginIndex+=1;
            }
            if (specialFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilter.setString(beginIndex, specialFilterComboBox.getSelectionModel().getSelectedItem().toString());
                beginIndex+=1;
            }
            ResultSet rs = sqlFilter.executeQuery();

            while (rs.next()) {
                data.add(new Person(rs.getInt("id"),
                rs.getString("login"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getDate("birthday"),
                rs.getString("birthdayplace"),
                rs.getString("gender"),
                rs.getString("role"),
                rs.getString("special"),
                rs.getLong("inn"),
                rs.getLong("snils"),
                rs.getBoolean("status"),
                rs.getString("secondname"),
                rs.getString("name"),
                rs.getString("family"),
                rs.getString("phone"),
                rs.getLong("passseries"),
                rs.getLong("passnum"),
                rs.getDate("passdateissue"),
                rs.getString("passissueplace"),
                rs.getInt("passdepartmentcode"))); 
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
