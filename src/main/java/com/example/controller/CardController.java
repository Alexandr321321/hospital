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
import com.example.Card;
import com.example.Person;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

public class CardController {

    private Card selectedItem;

    private Boolean action;

    ObservableList<Card> data;

    @FXML
    private Label actionLabel;

    @FXML
    private TextField alergenEditTextField;

    @FXML
    private DatePicker birthDayEditDatePicker;

    @FXML
    private TextField birthPlaceEditTextField;

    @FXML
    private TableColumn cardTable;

    @FXML
    private ComboBox educationEditComboBox;

    @FXML
    private TextField enpEditTextField;

    @FXML
    private TextField enterpriceEditTextField;

    @FXML
    private TextField familyEditTextField;

    @FXML
    private TextField firstNameEditTextField;

    @FXML
    private TextField firstNameZagEditTextField;

    @FXML
    private TextField cardNumEditTextField;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton genderManEditRadioButton;

    @FXML
    private RadioButton genderWomanEditRadioButton;

    @FXML
    private TableColumn idTable;

    @FXML
    private TextField innEditTextField;

    @FXML
    private TextField jobplaceEditTextField;

    @FXML
    private DatePicker passDateIssueEditDatePicker;

    @FXML
    private DatePicker passDateIssueZagEditDatePicker;

    @FXML
    private TextField passDepartmentCodeEditTextField;

    @FXML
    private TextField passIssuePlaceEditTextField;

    @FXML
    private TextField passNumEditTextField;

    @FXML
    private TextField passNumZagEditTextField;

    @FXML
    private TextField passSeriesEditTextField;

    @FXML
    private TextField passSeriesZagEditTextField;

    @FXML
    private TextField phoneEditTextField;

    @FXML
    private TableColumn polisTable;

    @FXML
    private TableColumn secondNameTable;

    @FXML
    private TableColumn firstNameTable;

    @FXML
    private TableColumn familyTable;

    @FXML
    private TableColumn birthdayTable;

    @FXML
    private TableColumn phoneTable;

    @FXML
    private TextField professionEditTextField;

    @FXML
    private TextField secondNameEditTextField;

    @FXML
    private TextField secondNameZagEditTextField;

    @FXML
    private TextField snilsEditTextField;

    @FXML
    private TextField svidSeriesEditTextField;

    @FXML
    private TextField svidNumEditTextField;

    @FXML
    private DatePicker svidDateIssueEditDatePicker;

    @FXML
    private TextField svidIssuePlaceEditTextField;

    @FXML
    private TextField educationPlaceEditTextField;


    @FXML
    private TextField cardFilterTextField;

    @FXML
    private TextField enpFilterTextField;

    @FXML
    private TextField secondNameFilterTextField;

    @FXML
    private TextField firstNameFilterTextField;

    @FXML
    private TextField familyFilterTextField;

    @FXML
    private DatePicker birthDayFilterDatePicker;

    @FXML
    private TextField phoneFilterTextField;

    @FXML
    private TableView table;

    public void initialize() throws IOException, SQLException {
        ObservableList<String> education = FXCollections.observableArrayList(App.getProperties("education"));
        educationEditComboBox.getItems().addAll(education);


        data = FXCollections.observableArrayList();

        idTable.setCellValueFactory(new PropertyValueFactory<>("id"));
        cardTable.setCellValueFactory(new PropertyValueFactory<>("cardnumber"));
        polisTable.setCellValueFactory(new PropertyValueFactory<>("enp"));
        secondNameTable.setCellValueFactory(new PropertyValueFactory<>("secondname"));
        firstNameTable.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        familyTable.setCellValueFactory(new PropertyValueFactory<>("family"));
        birthdayTable.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        phoneTable.setCellValueFactory(new PropertyValueFactory<>("phone"));

        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        PreparedStatement sqlSelect = conn.prepareStatement("SELECT * FROM card");
        ResultSet rs = sqlSelect.executeQuery();

        while (rs.next()) {
            data.add(new Card(rs.getInt("id"),
            rs.getInt("cardnumber"),
            rs.getString("secondname"),
            rs.getString("name"),
            rs.getString("family"),
            rs.getString("phone"),
            rs.getDate("birthday"),
            rs.getString("gender"),
            rs.getInt("passseries"),
            rs.getInt("passnum"),
            rs.getDate("passdateissue"),
            rs.getString("passissueplace"),
            rs.getInt("passdepartmentcode"),
            rs.getString("svidseries"),
            rs.getInt("svidnum"),
            rs.getDate("sviddateissue"),
            rs.getString("svidissueplace"),
            rs.getInt("inn"),
            rs.getInt("snils"),
            rs.getInt("enp"),
            rs.getString("jobplace"),
            rs.getString("profession"),
            rs.getString("enterprice"),
            rs.getString("education"),
            rs.getString("educationplace"),
            rs.getString("alergen"),
            rs.getString("secondnamezag"),
            rs.getString("firstnamezag"),
            rs.getInt("passserieszag"),
            rs.getInt("passnumzag"),
            rs.getDate("passdateissuezag"),
            rs.getString("birthplace"))); 
            table.setItems(data);
        }
        conn.close();
        rs.close();
    }

    @FXML
    private void selectCard() throws IOException {
       selectedItem = (Card) table.getSelectionModel().getSelectedItem();
       if (selectedItem != null) {
        actionLabel.setText("изменение ID = " + selectedItem.getId());
        action = false;
        cardNumEditTextField.setText(selectedItem.getCardnumber().toString());
        secondNameEditTextField.setText(selectedItem.getSecondname());
        firstNameEditTextField.setText(selectedItem.getFirstname());
        familyEditTextField.setText(selectedItem.getFamily());
        phoneEditTextField.setText(selectedItem.getPhone());
        birthDayEditDatePicker.setValue(App.convertToLocalDateViaSqlDate(selectedItem.getBirthday()));
        if (selectedItem.getGender().equals("Мужской")) {
            genderManEditRadioButton.setSelected(true);
        }
        if (selectedItem.getGender().equals("Женский")){
            genderWomanEditRadioButton.setSelected(true);
        }
        birthPlaceEditTextField.setText(selectedItem.getBirthplace());
        passSeriesEditTextField.setText(selectedItem.getPassseries().toString());
        passNumEditTextField.setText(selectedItem.getPassnum().toString());
        passDateIssueEditDatePicker.setValue(App.convertToLocalDateViaSqlDate(selectedItem.getPassdateissue()));
        passDepartmentCodeEditTextField.setText(selectedItem.getPassdepartmentcode().toString());
        passIssuePlaceEditTextField.setText(selectedItem.getPassissueplace());
        secondNameZagEditTextField.setText(selectedItem.getSecondnamezag());
        firstNameZagEditTextField.setText(selectedItem.getFirstnamezag());

        passSeriesZagEditTextField.setText(selectedItem.getPassserieszag().toString());
        passNumZagEditTextField.setText(selectedItem.getPassnumzag().toString());
        passDateIssueZagEditDatePicker.setValue(App.convertToLocalDateViaSqlDate(selectedItem.getPassdateissuezag()));

        svidSeriesEditTextField.setText(selectedItem.getSvidseries());
        svidNumEditTextField.setText(selectedItem.getSvidnum().toString());
        svidDateIssueEditDatePicker.setValue(App.convertToLocalDateViaSqlDate(selectedItem.getSviddateissue()));
        svidIssuePlaceEditTextField.setText(selectedItem.getSvidissueplace());

        innEditTextField.setText(selectedItem.getInn().toString());
        snilsEditTextField.setText(selectedItem.getSnils().toString());
        enpEditTextField.setText(selectedItem.getEnp().toString());

        jobplaceEditTextField.setText(selectedItem.getJobplace());
        professionEditTextField.setText(selectedItem.getProfession());
        enterpriceEditTextField.setText(selectedItem.getEnterprice());
        educationEditComboBox.getSelectionModel().select(selectedItem.getEducation());
        educationPlaceEditTextField.setText(selectedItem.getEducationplace());
        alergenEditTextField.setText(selectedItem.getAlergen());
        
       }
    }

    @FXML
    private void selectActionNewCard() {
        action = true;
        actionLabel.setText("новая карта");
    }

    @FXML
    private void saveCardData() throws NumberFormatException, SQLException, IOException {
        DataSource dataSource = App.createDataSource();

        Connection conn = dataSource.getConnection();

        if (action) {
            try {
                PreparedStatement sqlInsert = conn.prepareStatement("INSERT INTO public.card (cardnumber, secondname, name, family, phone, birthday, gender, birthplace, passseries, passnum, passdateissue, passissueplace, passdepartmentcode, svidseries, svidnum, sviddateissue, svidissueplace, inn, snils, enp, jobplace, profession, enterprice, education, educationplace, alergen, secondnamezag, firstnamezag, passserieszag, passnumzag, passdateissuezag)\r\n" + //
                                        "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                sqlInsert.setLong(1, Long.parseLong(cardNumEditTextField.getText()));
                sqlInsert.setString(2, secondNameEditTextField.getText());
                sqlInsert.setString(3, firstNameEditTextField.getText());
                sqlInsert.setString(4, familyEditTextField.getText());
                sqlInsert.setString(5, phoneEditTextField.getText());
                sqlInsert.setDate(6, java.sql.Date.valueOf(birthDayEditDatePicker.getValue()));
                if (genderManEditRadioButton.isSelected()) {
                    sqlInsert.setString(7, "Мужской");
                }
                if (genderWomanEditRadioButton.isSelected()) {
                    sqlInsert.setString(7, "Женский");
                }
                sqlInsert.setString(8, birthPlaceEditTextField.getText());
                sqlInsert.setLong(9, Long.parseLong(passSeriesEditTextField.getText()));
                sqlInsert.setLong(10, Long.parseLong(passNumEditTextField.getText()));
                sqlInsert.setDate(11, java.sql.Date.valueOf(passDateIssueEditDatePicker.getValue()));
                sqlInsert.setString(12, passIssuePlaceEditTextField.getText());
                sqlInsert.setLong(13, Long.parseLong(passDepartmentCodeEditTextField.getText()));
                sqlInsert.setString(14, svidSeriesEditTextField.getText());
                sqlInsert.setLong(15, Long.parseLong(svidNumEditTextField.getText()));
                sqlInsert.setDate(16, java.sql.Date.valueOf(svidDateIssueEditDatePicker.getValue()));
                sqlInsert.setString(17, svidIssuePlaceEditTextField.getText());
                sqlInsert.setLong(18, Long.parseLong(innEditTextField.getText()));
                sqlInsert.setLong(19, Long.parseLong(snilsEditTextField.getText()));
                sqlInsert.setLong(20, Long.parseLong(enpEditTextField.getText()));
                sqlInsert.setString(21, jobplaceEditTextField.getText());
                sqlInsert.setString(22, professionEditTextField.getText());
                sqlInsert.setString(23, enterpriceEditTextField.getText());
                sqlInsert.setString(24, educationEditComboBox.getSelectionModel().getSelectedItem().toString());
                sqlInsert.setString(25, educationPlaceEditTextField.getText());
                sqlInsert.setString(26, alergenEditTextField.getText());
                sqlInsert.setString(27, secondNameZagEditTextField.getText());
                sqlInsert.setString(28, firstNameZagEditTextField.getText());
                sqlInsert.setLong(29, Long.parseLong(passSeriesZagEditTextField.getText()));
                sqlInsert.setLong(30, Long.parseLong(passNumZagEditTextField.getText()));
                sqlInsert.setDate(31, java.sql.Date.valueOf(passDateIssueZagEditDatePicker.getValue()));

                sqlInsert.executeUpdate();

                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Новая запись");
                sqlHistory.setString(2, "Карты");
                sqlHistory.setString(3, "-");
                sqlHistory.setString(4, "-");
                sqlHistory.setString(5, "-");
                sqlHistory.setInt(6, App.getUserId());
                sqlHistory.setObject(7, OffsetDateTime.now());
                sqlHistory.executeUpdate();

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Добавлена новая карта!");
                alert.showAndWait();

                conn.close();
                App.setRoot("card");
            
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
                PreparedStatement sqlUpdate = conn.prepareStatement("UPDATE public.card SET cardnumber=?, secondname=?, name=?, family=?, phone=?, birthday=?, gender=?, birthplace=?, passseries=?, passnum=?, passdateissue=?, passissueplace=?, passdepartmentcode=?, svidseries=?, svidnum=?, sviddateissue=?, svidissueplace=?, inn=?, snils=?, enp=?, jobplace=?, profession=?, enterprice=?, education=?, educationplace=?, alergen=?, secondnamezag=?, firstnamezag=?, passserieszag=?, passnumzag=?, passdateissuezag=? WHERE id=?");
                sqlUpdate.setLong(1, Long.parseLong(cardNumEditTextField.getText()));
                sqlUpdate.setString(2, secondNameEditTextField.getText());
                sqlUpdate.setString(3, firstNameEditTextField.getText());
                sqlUpdate.setString(4, familyEditTextField.getText());
                sqlUpdate.setString(5, phoneEditTextField.getText());
                sqlUpdate.setDate(6, java.sql.Date.valueOf(birthDayEditDatePicker.getValue()));
                if (genderManEditRadioButton.isSelected()) {
                    sqlUpdate.setString(7, "Мужской");
                }
                if (genderWomanEditRadioButton.isSelected()) {
                    sqlUpdate.setString(7, "Женский");
                }
                sqlUpdate.setString(8, birthPlaceEditTextField.getText());
                sqlUpdate.setLong(9, Long.parseLong(passSeriesEditTextField.getText()));
                sqlUpdate.setLong(10, Long.parseLong(passNumEditTextField.getText()));
                sqlUpdate.setDate(11, java.sql.Date.valueOf(passDateIssueEditDatePicker.getValue()));
                sqlUpdate.setString(12, passIssuePlaceEditTextField.getText());
                sqlUpdate.setLong(13, Long.parseLong(passDepartmentCodeEditTextField.getText()));
                sqlUpdate.setString(14, svidSeriesEditTextField.getText());
                sqlUpdate.setLong(15, Long.parseLong(svidNumEditTextField.getText()));
                sqlUpdate.setDate(16, java.sql.Date.valueOf(svidDateIssueEditDatePicker.getValue()));
                sqlUpdate.setString(17, svidIssuePlaceEditTextField.getText());
                sqlUpdate.setLong(18, Long.parseLong(innEditTextField.getText()));
                sqlUpdate.setLong(19, Long.parseLong(snilsEditTextField.getText()));
                sqlUpdate.setLong(20, Long.parseLong(enpEditTextField.getText()));
                sqlUpdate.setString(21, jobplaceEditTextField.getText());
                sqlUpdate.setString(22, professionEditTextField.getText());
                sqlUpdate.setString(23, enterpriceEditTextField.getText());
                sqlUpdate.setString(24, educationEditComboBox.getSelectionModel().getSelectedItem().toString());
                sqlUpdate.setString(25, educationPlaceEditTextField.getText());
                sqlUpdate.setString(26, alergenEditTextField.getText());
                sqlUpdate.setString(27, secondNameZagEditTextField.getText());
                sqlUpdate.setString(28, firstNameZagEditTextField.getText());
                sqlUpdate.setLong(29, Long.parseLong(passSeriesZagEditTextField.getText()));
                sqlUpdate.setLong(30, Long.parseLong(passNumZagEditTextField.getText()));
                sqlUpdate.setDate(31, java.sql.Date.valueOf(passDateIssueZagEditDatePicker.getValue()));
                sqlUpdate.setInt(32, selectedItem.getId());

                sqlUpdate.executeUpdate();

                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Изменение");
                sqlHistory.setString(2, "Карты");
                sqlHistory.setString(3, selectedItem.getId().toString());
                sqlHistory.setString(4, selectedItem.getId().toString());
                sqlHistory.setString(5, selectedItem.getId().toString());
                sqlHistory.setInt(6, App.getUserId());
                sqlHistory.setObject(7, OffsetDateTime.now());
                sqlHistory.executeUpdate();
                conn.close();
                App.setRoot("card");
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Уведомление");
                alert.setHeaderText(null);
                alert.setContentText("Проверьте заполненость полей! Ошибка: " + e);
                alert.showAndWait();
            }
        } 
    }

    @FXML
    private void deleteCardData() throws SQLException, IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Уведомление");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены, что хотите удалить ID = " + selectedItem.getId());
        Optional <ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            try {
                DataSource dataSource = App.createDataSource();
                Connection conn = dataSource.getConnection();
                PreparedStatement sqlDelete = conn.prepareStatement("DELETE FROM public.card WHERE id=?");
                sqlDelete.setInt(1, selectedItem.getId());
                sqlDelete.executeUpdate();
                PreparedStatement sqlHistory = conn.prepareStatement("INSERT INTO public.history (type, tablename, field, previousvalue, newvalue, personid, datetime) VALUES (?, ?, ?, ?, ?, ?, ?)");
                sqlHistory.setString(1, "Удаление");
                sqlHistory.setString(2, "Карты");
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
                alert2.setContentText("Удалена карта с ID = " + selectedItem.getId() + "!");
                alert2.showAndWait();
                App.setRoot("card");
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
    private void filterCard() throws NumberFormatException, SQLException, IOException {

        if (!cardFilterTextField.getText().isEmpty() || !enpFilterTextField.getText().isEmpty() || !secondNameFilterTextField.getText().isEmpty() || !firstNameFilterTextField.getText().isEmpty() || !familyFilterTextField.getText().isEmpty() || !phoneFilterTextField.getText().isEmpty() || birthDayFilterDatePicker.getValue() != null) {
            
            String sqlFilterString;

            //Счетчик индекса для preparedstatement
            Integer beginIndex = 1;

            data.clear();

            DataSource dataSource = App.createDataSource();

            Connection conn = dataSource.getConnection();

            PreparedStatement sqlFilter;

            sqlFilterString = "SELECT * FROM public.card WHERE ";

            if (!cardFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "cardnumber = ? AND ";
            }
            if (!enpFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "enp = ? AND ";
            }
            if (!secondNameFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "secondname = ? AND ";
            }
            if (!firstNameFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "name = ? AND ";
            }
            if (!familyFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "family = ? AND ";
            }
            if (!phoneFilterTextField.getText().isEmpty()) {
                sqlFilterString+= "phone = ? AND ";
            }
            if (birthDayFilterDatePicker.getValue() != null) {
                sqlFilterString+= "birthday = ? AND ";
            }

            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);
            sqlFilterString = sqlFilterString.substring(0, sqlFilterString.length()-1);

            sqlFilter = conn.prepareStatement(sqlFilterString);

            if (!cardFilterTextField.getText().isEmpty()) {
                sqlFilter.setInt(beginIndex, Integer.valueOf(cardFilterTextField.getText()));
                beginIndex+=1;
            }
            if (!enpFilterTextField.getText().isEmpty()) {
                sqlFilter.setLong(beginIndex, Long.parseLong(enpFilterTextField.getText()));
                beginIndex+=1;
            }
            if (!secondNameFilterTextField.getText().isEmpty()) {
                sqlFilter.setString(beginIndex, secondNameFilterTextField.getText());
                beginIndex+=1;
            }
            if (!firstNameFilterTextField.getText().isEmpty()) {
                sqlFilter.setString(beginIndex, firstNameFilterTextField.getText());
                beginIndex+=1;
            }
            if (!familyFilterTextField.getText().isEmpty()) {
                sqlFilter.setString(beginIndex, familyFilterTextField.getText());
                beginIndex+=1;
            }
            if (!phoneFilterTextField.getText().isEmpty()) {
                sqlFilter.setString(beginIndex, phoneFilterTextField.getText());
                beginIndex+=1;
            }
            if (birthDayFilterDatePicker.getValue() != null) {
                sqlFilter.setDate(beginIndex, java.sql.Date.valueOf(birthDayFilterDatePicker.getValue()));
                beginIndex+=1;
            }

            
            ResultSet rs = sqlFilter.executeQuery();

            while (rs.next()) {
                data.add(new Card(rs.getInt("id"),
                rs.getInt("cardnumber"),
                rs.getString("secondname"),
                rs.getString("name"),
                rs.getString("family"),
                rs.getString("phone"),
                rs.getDate("birthday"),
                rs.getString("gender"),
                rs.getInt("passseries"),
                rs.getInt("passnum"),
                rs.getDate("passdateissue"),
                rs.getString("passissueplace"),
                rs.getInt("passdepartmentcode"),
                rs.getString("svidseries"),
                rs.getInt("svidnum"),
                rs.getDate("sviddateissue"),
                rs.getString("svidissueplace"),
                rs.getInt("inn"),
                rs.getInt("snils"),
                rs.getInt("enp"),
                rs.getString("jobplace"),
                rs.getString("profession"),
                rs.getString("enterprice"),
                rs.getString("education"),
                rs.getString("educationplace"),
                rs.getString("alergen"),
                rs.getString("secondnamezag"),
                rs.getString("firstnamezag"),
                rs.getInt("passserieszag"),
                rs.getInt("passnumzag"),
                rs.getDate("passdateissuezag"),
                rs.getString("birthplace"))); 
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
