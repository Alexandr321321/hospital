module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.controlsfx.controls;

    opens com.example to javafx.fxml, org.controlsfx.controls;
    opens com.example.controller to javafx.fxml, org.controlsfx.controls;
    exports com.example;
}
