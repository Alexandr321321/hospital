package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;

import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Scene modalScene;
    private static Integer userId1;

    public static void setUserId (Integer userId) {
        userId1 = userId;
    }

    public static Integer getUserId() {
        return userId1;
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"));
        stage.setScene(scene);
        stage.setTitle("МЕДок");
        stage.show();
    }

    public static DataSource createDataSource() {
        final String url = "jdbc:postgresql://localhost:5432/hospital?user=postgres&password=w123456789";
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static String[] getProperties(String key) throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream("src\\main\\java\\prop.xml");
        properties.loadFromXML(in);
        String[]values = properties.get(key).toString().split("#");
        return values;
    }

    public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public static void main(String[] args) {
        launch();
    }

}