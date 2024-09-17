package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.controlsfx.control.RangeSlider;

import com.example.App;
import com.example.Job;
import com.example.Message;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class JobController {

    private Job selectedItem;

    private Boolean action;

    ObservableList<Job> data;

    @FXML
    private ComboBox personFilterComboBox;

    @FXML
    private DatePicker dateFilterDatePicker;

    @FXML
    private ComboBox personEditComboBox;

    @FXML
    private Label actionLabel;


    @FXML
    private TableView table;

    @FXML
    private TableColumn idTable;

    @FXML
    private TableColumn loginTable;

    @FXML
    private TableColumn timeStartTable;

    @FXML
    private TableColumn timeFinishTable;

    @FXML
    private TableColumn specialTable;

    @FXML
    private RangeSlider timeEditRangeSlider;

    @FXML
    private DatePicker dateEditDatePicker;


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
        personEditComboBox.getItems().addAll(person);
        personFilterComboBox.getItems().addAll(person);

        

        idTable.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginTable.setCellValueFactory(new PropertyValueFactory<>("personid"));
        timeStartTable.setCellValueFactory(new PropertyValueFactory<>("timestart"));
        timeFinishTable.setCellValueFactory(new PropertyValueFactory<>("timefinish"));
        specialTable.setCellValueFactory(new PropertyValueFactory<>("special"));
        
        PreparedStatement sqlSelect = conn.prepareStatement("SELECT job.id, job.personid, job.datetimerange, lower(job.datetimerange) as lower_datetimerange, upper(job.datetimerange) as upper_datetimerange, person.login, person.special FROM job\r\n" + //
                        "\tJOIN person ON job.personid = person.id");
        ResultSet rs = sqlSelect.executeQuery();
        while (rs.next()) {
            data.add(new Job(rs.getInt("id"),
            rs.getString("login"),
            rs.getTimestamp("lower_datetimerange"),
            rs.getTimestamp("upper_datetimerange"),
            rs.getString("special")));
            table.setItems(data);
        }
        conn.close();
        rs.close();
    }

    @FXML
    private void selectJob() throws IOException {
       selectedItem = (Job) table.getSelectionModel().getSelectedItem();
       if (selectedItem != null) {
        actionLabel.setText("изменение ID = " + selectedItem.getId());
        action = false;
        personEditComboBox.getSelectionModel().select(selectedItem.getPersonid());
        dateEditDatePicker.setValue(selectedItem.getTimestart().toLocalDateTime().toLocalDate());
        timeEditRangeSlider.setLowValue(selectedItem.getTimestart().getHours());
        timeEditRangeSlider.setHighValue(selectedItem.getTimefinish().getHours());
       }
    }

    @FXML
    private void selectActionNewJob() {
        action = true;
        actionLabel.setText("новая запись");
    }

    @FXML
    private void saveJobData() throws NumberFormatException, SQLException, IOException {
        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        if (action) {
            try {
                PreparedStatement sqlSelectPersonId = conn.prepareStatement("SELECT id FROM public.person WHERE login=?");
                sqlSelectPersonId.setString(1, personEditComboBox.getSelectionModel().getSelectedItem().toString());
                ResultSet rs = sqlSelectPersonId.executeQuery();

                while (rs.next()) {
                    PreparedStatement sqlInsert = conn.prepareStatement("INSERT INTO public.job (personid, datetimerange) VALUES (?, ?)");
                    sqlInsert.setInt(1, rs.getInt("id"));


                    LocalDate date = dateEditDatePicker.getValue();

                    String startTime = date.toString() + " " + (int) (timeEditRangeSlider.getLowValue() - 3) + ":00:00+00";
                    String endTime = date.toString() + " " + (int) (timeEditRangeSlider.getHighValue() - 3) + ":00:00+00";

                    String tstzrangeValue = "[" + startTime + "," + endTime + "]";

                    sqlInsert.setObject(2, tstzrangeValue, Types.OTHER);
                    sqlInsert.executeUpdate();
                    PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    sqlHistory.setString(1, "Новая запись");
                    sqlHistory.setString(2, "График работы");
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
                }
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
                PreparedStatement sqlSelectPersonId = conn.prepareStatement("SELECT id FROM public.person WHERE login=?");
                sqlSelectPersonId.setString(1, personEditComboBox.getSelectionModel().getSelectedItem().toString());
                ResultSet rs = sqlSelectPersonId.executeQuery();

                while (rs.next()) {
                    PreparedStatement sqlUpdate = conn.prepareStatement("UPDATE public.job SET personid=?, datetimerange=? WHERE id=?");
                    sqlUpdate.setInt(1, rs.getInt("id"));


                    LocalDate date = dateEditDatePicker.getValue();

                    String startTime = date.toString() + " " + (int) (timeEditRangeSlider.getLowValue() - 3) + ":00:00+00";
                    String endTime = date.toString() + " " + (int) (timeEditRangeSlider.getHighValue() - 3) + ":00:00+00";

                    String tstzrangeValue = "[" + startTime + "," + endTime + "]";

                    sqlUpdate.setObject(2, tstzrangeValue, Types.OTHER);
                    sqlUpdate.setInt(3, selectedItem.getId());
                    sqlUpdate.executeUpdate();
                    PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    sqlHistory.setString(1, "Изменение");
                    sqlHistory.setString(2, "График работы");
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
                }
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Проверьте заполненость полей! Ошибка: " + e);
                alert.showAndWait();
            }
        }  
        conn.close();
        App.setRoot("job");
    }

    @FXML
    private void deleteJobData() throws SQLException, IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Уведомление");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите удалить ID = " + selectedItem.getId());
        Optional <ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            try {
                DataSource dataSource = App.createDataSource();
                Connection conn = dataSource.getConnection();
                PreparedStatement sqlDelete = conn.prepareStatement("DELETE FROM public.job WHERE id=?");
                sqlDelete.setInt(1, selectedItem.getId());
                sqlDelete.executeUpdate();
                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Удаление");
                sqlHistory.setString(2, "График работы");
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
                alert2.setContentText("Удалена запись с ID = " + selectedItem.getId() + "!");
                alert2.showAndWait();
                App.setRoot("job");
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
    private void filterJob() throws NumberFormatException, SQLException, IOException {

        if (personFilterComboBox.getSelectionModel().getSelectedItem() != null || dateFilterDatePicker.getValue() != null) {
            
            String sqlFilterString;

            //Счетчик индекса для preparedstatement
            Integer beginIndex = 1;

            data.clear();

            DataSource dataSource = App.createDataSource();

            Connection conn = dataSource.getConnection();

            PreparedStatement sqlFilter;

            sqlFilterString = "SELECT job.id, job.personid, job.datetimerange, lower(job.datetimerange) as lower_datetimerange, upper(job.datetimerange) as upper_datetimerange, person.login, person.special FROM job JOIN person ON job.personid = person.id WHERE ";

            if (personFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilterString+= "personid = ? AND ";
            }
            if (dateFilterDatePicker.getValue() != null) {
                sqlFilterString+= "tstzrange(date_trunc('day', lower(datetimerange)), date_trunc('day', upper(datetimerange)), '[]') @> ?::timestamp with time zone AND ";
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
                    sqlFilter.setInt(beginIndex, rs.getInt("id"));
                }
                beginIndex+=1;
            }
            if (dateFilterDatePicker.getValue() != null) {
                sqlFilter.setDate(beginIndex, java.sql.Date.valueOf(dateFilterDatePicker.getValue()));
                beginIndex+=1;
            }
            ResultSet rs = sqlFilter.executeQuery();

            while (rs.next()) {
                data.add(new Job(rs.getInt("id"),
            rs.getString("login"),
            rs.getTimestamp("lower_datetimerange"),
            rs.getTimestamp("upper_datetimerange"),
            rs.getString("special")));
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
