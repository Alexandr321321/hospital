package com.example.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.controlsfx.control.RangeSlider;
import org.controlsfx.control.SearchableComboBox;

import com.example.App;
import com.example.Contract;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ContractController {

    private Contract selectedItem;

    private Boolean action;

    ObservableList<Contract> data;

    @FXML
    private Label actionLabel;
    
    @FXML
    private TableView table;

    @FXML
    private TableColumn idTable;
    
    @FXML
    private TableColumn cardIdTable;

    @FXML
    private TableColumn personIdTable;

    @FXML
    private TableColumn serviceIdTable;

    @FXML
    private TableColumn timeStartTable;

    @FXML
    private TableColumn timeFinishTable;

    @FXML
    private ComboBox personFilterComboBox;

    @FXML
    private DatePicker dateFilterDatePicker;

    @FXML
    private TextField cardIdFilterTextField;

    @FXML
    private TextField idFilterTextField;

    @FXML
    private TextField secondNameFilterTextField;

    @FXML
    private TextField firstNameFilterTextField;

    @FXML
    private TextField familyFilterTextField;

    @FXML
    private TextField birthDayFilterTextField;

    @FXML
    private TextField passSeriesFilterTextField;

    @FXML
    private TextField passNumFilterTextField;

    @FXML
    private TextField idEditTextField;

    @FXML
    private ComboBox personEditComboBox;

    @FXML
    private DatePicker dateEditDatePicker;

    @FXML
    private SearchableComboBox serviceEditSearchableComboBox;

    @FXML
    private TextField sumEditTextField;

    @FXML
    private TextField roomEditTextField;

    @FXML
    private TextField contractNumEditTextField;

    @FXML
    private ComboBox payTypeEditComboBox;

    @FXML
    private CheckBox statusEditCheckBox;

    @FXML
    private RangeSlider hourEditRangeSlider;

    @FXML
    private RangeSlider minuteEditRangeSlider;

    List<String> person = new ArrayList<String>();
    List<String> service = new ArrayList<String>();

    public void initialize() throws IOException, SQLException {
        data = FXCollections.observableArrayList();

        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        PreparedStatement sqlSelectPerson = conn.prepareStatement("SELECT * FROM person WHERE role='Врач'");
        ResultSet rsPerson = sqlSelectPerson.executeQuery();

        while (rsPerson.next()) {
            person.add(rsPerson.getString("login"));
        }
        personFilterComboBox.getItems().addAll(person);
        personEditComboBox.getItems().addAll(person);

        PreparedStatement sqlSelectService = conn.prepareStatement("SELECT * FROM service");
        ResultSet rsService = sqlSelectService.executeQuery();

        while (rsService.next()) {
            service.add(rsService.getString("name"));
        }
        serviceEditSearchableComboBox.getItems().addAll(service);
        ObservableList<String> payType = FXCollections.observableArrayList("Наличный", "Безналичный");
        payTypeEditComboBox.getItems().addAll(payType);

        idTable.setCellValueFactory(new PropertyValueFactory<>("id"));
        cardIdTable.setCellValueFactory(new PropertyValueFactory<>("cardid"));
        personIdTable.setCellValueFactory(new PropertyValueFactory<>("personid"));
        serviceIdTable.setCellValueFactory(new PropertyValueFactory<>("serviceid"));
        timeStartTable.setCellValueFactory(new PropertyValueFactory<>("timestart"));
        timeFinishTable.setCellValueFactory(new PropertyValueFactory<>("timefinish"));

        PreparedStatement sqlSelect = conn.prepareStatement("SELECT contract.id, contract.cardid, contract.personid, contract.serviceid, contract.contractnum, contract.sum, contract.room, contract.paytype, contract.status, contract.datetimerange, lower(contract.datetimerange) as lower_datetimerange, upper(contract.datetimerange) as upper_datetimerange, person.login, service.name\r\n" + //
                        "\tFROM public.contract\r\n" + //
                        "\tJOIN person ON contract.personid = person.id\r\n" + //
                        "\tJOIN service ON contract.serviceid = service.id");

        ResultSet rs = sqlSelect.executeQuery();
        while (rs.next()) {
            data.add(new Contract(rs.getInt("id"),
            rs.getInt("cardid"),
            rs.getString("login"),
            rs.getString("name"),
            rs.getInt("contractnum"),
            rs.getTimestamp("lower_datetimerange"),
            rs.getTimestamp("upper_datetimerange"),
            rs.getDouble("sum"),
            rs.getInt("room"),
            rs.getString("paytype"),
            rs.getBoolean("status")));
            table.setItems(data);
        }

        serviceEditSearchableComboBox.setOnAction(e -> {
            try {
                setCostValue();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        conn.close();
        rs.close();
    }

    @FXML
    private void selectContract() throws IOException {
       selectedItem = (Contract) table.getSelectionModel().getSelectedItem();
       if (selectedItem != null) {
        actionLabel.setText("изменение ID = " + selectedItem.getId());
        action = false;
        idEditTextField.setText(selectedItem.getCardid().toString());
        personEditComboBox.getSelectionModel().select(selectedItem.getPersonid());
        dateEditDatePicker.setValue(selectedItem.getTimestart().toLocalDateTime().toLocalDate());
        serviceEditSearchableComboBox.getSelectionModel().select(selectedItem.getServiceid());
        sumEditTextField.setText(selectedItem.getSum().toString());
        roomEditTextField.setText(selectedItem.getRoom().toString());
        contractNumEditTextField.setText(selectedItem.getContractnum().toString());
        payTypeEditComboBox.getSelectionModel().select(selectedItem.getPaytype());
        statusEditCheckBox.setSelected(selectedItem.getStatus());
        hourEditRangeSlider.setLowValue(selectedItem.getTimestart().getHours());
        hourEditRangeSlider.setHighValue(selectedItem.getTimefinish().getHours());
        if (selectedItem.getTimestart().getMinutes() < selectedItem.getTimefinish().getMinutes()) {
            minuteEditRangeSlider.setLowValue(selectedItem.getTimestart().getMinutes());
            minuteEditRangeSlider.setHighValue(selectedItem.getTimefinish().getMinutes());
        } else {
            minuteEditRangeSlider.setLowValue(selectedItem.getTimefinish().getMinutes());
            minuteEditRangeSlider.setHighValue(selectedItem.getTimestart().getMinutes());
        }
       }
    }

    @FXML
    private void selectActionNewContract() {
        action = true;
        actionLabel.setText("новая запись");
    }

    public void setCostValue() throws SQLException {
        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();
        if (serviceEditSearchableComboBox.getSelectionModel().getSelectedItem() != null) {
            PreparedStatement sqlGetCost = conn.prepareStatement("SELECT cost FROM public.service WHERE name=?");
            sqlGetCost.setString(1, serviceEditSearchableComboBox.getSelectionModel().getSelectedItem().toString());
            ResultSet rsCost = sqlGetCost.executeQuery();
            while (rsCost.next()) {
                sumEditTextField.setText(String.valueOf(rsCost.getDouble("cost")));
                break;
            }
        }
    }

    @FXML
    private void saveContractData() throws NumberFormatException, SQLException, IOException { 
        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        if (action) {
            try {
                PreparedStatement sqlSelectPersonId = conn.prepareStatement("SELECT id FROM public.person WHERE login=?");
                sqlSelectPersonId.setString(1, personEditComboBox.getSelectionModel().getSelectedItem().toString());
                ResultSet rsPerson = sqlSelectPersonId.executeQuery();

                while (rsPerson.next()) {
                    PreparedStatement sqlInsert = conn.prepareStatement("INSERT INTO public.contract (cardid, personid, serviceid, contractnum, sum, room, paytype, status, datetimerange) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    sqlInsert.setInt(1, Integer.valueOf(idEditTextField.getText()));
                    sqlInsert.setInt(2, rsPerson.getInt("id"));

                    PreparedStatement sqlSelectServiceId = conn.prepareStatement("SELECT id FROM public.service WHERE name=?");
                    sqlSelectServiceId.setString(1, serviceEditSearchableComboBox.getSelectionModel().getSelectedItem().toString());
                    System.out.println(serviceEditSearchableComboBox.getSelectionModel().getSelectedItem().toString());
                    ResultSet rsService = sqlSelectServiceId.executeQuery();
                    while (rsService.next()) {
                        System.out.println(rsService.getInt("id"));
                        sqlInsert.setInt(3, rsService.getInt("id"));
                    }

                    sqlInsert.setInt(4, Integer.valueOf(contractNumEditTextField.getText()));
                    sqlInsert.setDouble(5, Double.valueOf(sumEditTextField.getText()));
                    sqlInsert.setInt(6, Integer.valueOf(roomEditTextField.getText()));
                    sqlInsert.setString(7, payTypeEditComboBox.getSelectionModel().getSelectedItem().toString());
                    sqlInsert.setBoolean(8, statusEditCheckBox.isSelected());

                    LocalDate date = dateEditDatePicker.getValue();

                    String startTime = date.toString() + " " + (int) (hourEditRangeSlider.getLowValue() - 3) + ":" + (int) (minuteEditRangeSlider.getLowValue()) + ":00+00";
                    String endTime = date.toString() + " " + (int) (hourEditRangeSlider.getHighValue() - 3) + ":" + (int) (minuteEditRangeSlider.getHighValue()) + ":00+00";

                    String tstzrangeValue = "[" + startTime + "," + endTime + "]";

                    sqlInsert.setObject(9, tstzrangeValue, Types.OTHER);

                    sqlInsert.executeUpdate();
                    PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    sqlHistory.setString(1, "Новая запись");
                    sqlHistory.setString(2, "Договоры");
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
                    conn.close();
                    App.setRoot("contract");
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
                ResultSet rsPerson = sqlSelectPersonId.executeQuery();

                while (rsPerson.next()) {
                    PreparedStatement sqlUpdate = conn.prepareStatement("UPDATE public.contract SET cardid=?, personid=?, serviceid=?, contractnum=?, sum=?, room=?, paytype=?, status=?, datetimerange=? WHERE id=?");
                    sqlUpdate.setInt(1, Integer.valueOf(idEditTextField.getText()));
                    sqlUpdate.setInt(2, rsPerson.getInt("id"));
                    PreparedStatement sqlSelectServiceId = conn.prepareStatement("SELECT id FROM public.service WHERE name=?");
                    sqlSelectServiceId.setString(1, serviceEditSearchableComboBox.getSelectionModel().getSelectedItem().toString());
                    ResultSet rsService = sqlSelectServiceId.executeQuery();
                    while (rsService.next()) {
                        sqlUpdate.setInt(3, rsService.getInt("id"));
                    }
                    sqlUpdate.setInt(4, Integer.valueOf(contractNumEditTextField.getText()));
                    sqlUpdate.setDouble(5, Double.valueOf(sumEditTextField.getText()));
                    sqlUpdate.setInt(6, Integer.valueOf(roomEditTextField.getText()));
                    sqlUpdate.setString(7, payTypeEditComboBox.getSelectionModel().getSelectedItem().toString());
                    sqlUpdate.setBoolean(8, statusEditCheckBox.isSelected());

                    LocalDate date = dateEditDatePicker.getValue();

                    String startTime = date.toString() + " " + (int) (hourEditRangeSlider.getLowValue() - 3) + ":" + (int) (minuteEditRangeSlider.getLowValue()) + ":00+00";
                    String endTime = date.toString() + " " + (int) (hourEditRangeSlider.getHighValue() - 3) + ":" + (int) (minuteEditRangeSlider.getHighValue()) + ":00+00";

                    String tstzrangeValue = "[" + startTime + "," + endTime + "]";

                    sqlUpdate.setObject(9, tstzrangeValue, Types.OTHER);
                    sqlUpdate.setInt(10, selectedItem.getId());

                    sqlUpdate.executeUpdate();
                    PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    sqlHistory.setString(1, "Изменение");
                    sqlHistory.setString(2, "Договоры");
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
                    conn.close();
                    App.setRoot("contract");
                }
            }catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Проверьте заполненость полей! Ошибка: " + e);
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void deleteContractData() throws SQLException, IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Уведомление");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите удалить ID = " + selectedItem.getId());
        Optional <ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            try {
                DataSource dataSource = App.createDataSource();
                Connection conn = dataSource.getConnection();
                PreparedStatement sqlDelete = conn.prepareStatement("DELETE FROM public.contract WHERE id=?");
                sqlDelete.setInt(1, selectedItem.getId());
                sqlDelete.executeUpdate();
                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Удаление");
                sqlHistory.setString(2, "Договоры");
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
                App.setRoot("contract");
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
    private void filterContract() throws NumberFormatException, SQLException, IOException {

        if (personFilterComboBox.getSelectionModel().getSelectedItem() != null || dateFilterDatePicker.getValue() != null || !cardIdFilterTextField.getText().isEmpty()) {
            
            String sqlFilterString;

            //Счетчик индекса для preparedstatement
            Integer beginIndex = 1;

            data.clear();

            DataSource dataSource = App.createDataSource();

            Connection conn = dataSource.getConnection();

            PreparedStatement sqlFilter;

            sqlFilterString = "SELECT contract.id, contract.cardid, contract.personid, contract.serviceid, contract.contractnum, contract.sum, contract.room, contract.paytype, contract.status, contract.datetimerange, lower(contract.datetimerange) as lower_datetimerange, upper(contract.datetimerange) as upper_datetimerange, person.login, service.name\r\n" + //
                                "FROM public.contract\r\n" + //
                                "JOIN person ON contract.personid = person.id\r\n" + //
                                "JOIN service ON contract.serviceid = service.id\r\n" + //
                                "WHERE ";

            if (personFilterComboBox.getSelectionModel().getSelectedItem() != null) {
                sqlFilterString+= "personid = ? AND ";
            }
            if (dateFilterDatePicker.getValue() != null) {
                sqlFilterString+= "tstzrange(date_trunc('day', lower(datetimerange)), date_trunc('day', upper(datetimerange)), '[]') @> ?::timestamp with time zone AND ";
            }
            if (!cardIdFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "cardid = ? AND ";
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
            if (!cardIdFilterTextField.getText().isEmpty()) {
                sqlFilter.setInt(beginIndex, Integer.valueOf(cardIdFilterTextField.getText()));
                beginIndex+=1;
            }
            ResultSet rs = sqlFilter.executeQuery();

            while (rs.next()) {
                data.add(new Contract(rs.getInt("id"),
                rs.getInt("cardid"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getInt("contractnum"),
                rs.getTimestamp("lower_datetimerange"),
                rs.getTimestamp("upper_datetimerange"),
                rs.getDouble("sum"),
                rs.getInt("room"),
                rs.getString("paytype"),
                rs.getBoolean("status")));
                table.setItems(data);
            }
            if (!cardIdFilterTextField.getText().isEmpty()) {
                PreparedStatement slqGetCard = conn.prepareStatement("SELECT * FROM public.card WHERE id=?");
                slqGetCard.setInt(1, Integer.valueOf(cardIdFilterTextField.getText()));
                ResultSet rsCard = slqGetCard.executeQuery();
                while (rsCard.next()) {
                    idFilterTextField.setText(String.valueOf(rsCard.getInt("id")));
                    secondNameFilterTextField.setText(rsCard.getString("secondname"));
                    firstNameFilterTextField.setText(rsCard.getString("name"));
                    familyFilterTextField.setText(rsCard.getString("family"));
                    birthDayFilterTextField.setText(String.valueOf(rsCard.getDate("birthday")));
                    passSeriesFilterTextField.setText(String.valueOf(rsCard.getInt("passseries")));
                    passNumFilterTextField.setText(String.valueOf(rsCard.getInt("passnum")));
                }
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
