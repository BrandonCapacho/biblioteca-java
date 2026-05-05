package com.mycompany.gestion_biblioteca.dao;

import com.mycompany.gestion_biblioteca.database.DatabaseConnection;
import com.mycompany.gestion_biblioteca.model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad Libro.
 * Realiza las operaciones CRUD directamente contra la base de datos SQLite.
 */
public class LibroDAO {

    private final Connection connection;

    public LibroDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Inserta un nuevo libro en la base de datos.
     *
     * @param libro libro a registrar
     * @return {@code true} si la inserción fue exitosa
     */
    public boolean insertar(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, anio_publicacion, ejemplares_disponibles) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setInt(3, libro.getAnioPublicacion());
            ps.setInt(4, libro.getEjemplaresDisponibles());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los libros registrados.
     *
     * @return lista de todos los libros
     */
    public List<Libro> obtenerTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros ORDER BY id DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libros.add(mapearLibro(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libros: " + e.getMessage());
        }
        return libros;
    }

    /**
     * Busca libros cuyo título contenga el texto proporcionado.
     *
     * @param titulo texto a buscar (coincidencia parcial)
     * @return lista de libros que coinciden
     */
    public List<Libro> buscarPorTitulo(String titulo) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE titulo LIKE ? ORDER BY id DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + titulo + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapearLibro(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar libros: " + e.getMessage());
        }
        return libros;
    }

    /**
     * Actualiza los datos de un libro existente.
     *
     * @param libro libro con los datos actualizados (debe incluir el id)
     * @return {@code true} si la actualización fue exitosa
     */
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, anio_publicacion = ?, ejemplares_disponibles = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setInt(3, libro.getAnioPublicacion());
            ps.setInt(4, libro.getEjemplaresDisponibles());
            ps.setInt(5, libro.getId());
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un libro por su identificador.
     *
     * @param id identificador del libro a eliminar
     * @return {@code true} si se eliminó correctamente
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mapea una fila del ResultSet a un objeto Libro.
     */
    private Libro mapearLibro(ResultSet rs) throws SQLException {
        return new Libro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getInt("anio_publicacion"),
                rs.getInt("ejemplares_disponibles")
        );
    }
}
