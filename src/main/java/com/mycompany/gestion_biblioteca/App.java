package com.mycompany.gestion_biblioteca;

import com.mycompany.gestion_biblioteca.database.DatabaseConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de la aplicación Sistema de Gestión de Biblioteca.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Inicializar la base de datos al arrancar
        DatabaseConnection.initializeDatabase();

        // Cargar la vista principal
        FXMLLoader loader = new FXMLLoader(App.class.getResource("libro_view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1000, 650);
        stage.setTitle("📚 Sistema de Gestión de Biblioteca");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}