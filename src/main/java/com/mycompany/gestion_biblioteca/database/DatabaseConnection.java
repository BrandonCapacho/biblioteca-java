package com.mycompany.gestion_biblioteca.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton para la conexión a la base de datos SQLite.
 * Proporciona una única instancia de conexión y se encarga de
 * inicializar la tabla de libros si no existe.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:biblioteca.db";
    private static DatabaseConnection instance;
    private Connection connection;

    /** Constructor privado — Singleton. */
    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Conexión a SQLite establecida correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia única de DatabaseConnection.
     *
     * @return instancia Singleton
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null || instance.isConnectionClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Retorna la conexión activa a la base de datos.
     *
     * @return conexión JDBC
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Inicializa la base de datos creando la tabla «libros» si no existe.
     */
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS libros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "titulo TEXT NOT NULL, "
                + "autor TEXT NOT NULL, "
                + "anio_publicacion INTEGER NOT NULL, "
                + "ejemplares_disponibles INTEGER NOT NULL"
                + ");";

        try (Statement stmt = getInstance().getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabla 'libros' verificada/creada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    /**
     * Verifica si la conexión está cerrada.
     */
    private boolean isConnectionClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }
}
