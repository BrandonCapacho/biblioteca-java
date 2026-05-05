module com.mycompany.gestion_biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.gestion_biblioteca to javafx.fxml;
    opens com.mycompany.gestion_biblioteca.controller to javafx.fxml;
    opens com.mycompany.gestion_biblioteca.model to javafx.base;

    exports com.mycompany.gestion_biblioteca;
}
