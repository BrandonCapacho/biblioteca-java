package com.mycompany.gestion_biblioteca.service;

import com.mycompany.gestion_biblioteca.dao.LibroDAO;
import com.mycompany.gestion_biblioteca.model.Libro;

import java.util.List;

/**
 * Capa de servicio para la gestión de libros.
 * Aplica validaciones de negocio antes de delegar al DAO.
 */
public class LibroService {

    private final LibroDAO libroDAO;

    public LibroService() {
        this.libroDAO = new LibroDAO();
    }

    /**
     * Registra un nuevo libro tras validar los datos de entrada.
     *
     * @param titulo                título del libro
     * @param autor                 autor del libro
     * @param anioPublicacion       año de publicación
     * @param ejemplaresDisponibles número de ejemplares
     * @return mensaje de resultado
     */
    public String registrarLibro(String titulo, String autor, int anioPublicacion, int ejemplaresDisponibles) {
        // Validaciones
        if (titulo == null || titulo.trim().isEmpty()) {
            return "El título no puede estar vacío.";
        }
        if (autor == null || autor.trim().isEmpty()) {
            return "El autor no puede estar vacío.";
        }
        if (anioPublicacion < 0 || anioPublicacion > 2100) {
            return "El año de publicación no es válido.";
        }
        if (ejemplaresDisponibles < 0) {
            return "El número de ejemplares no puede ser negativo.";
        }

        Libro libro = new Libro(titulo.trim(), autor.trim(), anioPublicacion, ejemplaresDisponibles);
        boolean exito = libroDAO.insertar(libro);

        return exito ? "✔ Libro registrado exitosamente." : "✘ Error al registrar el libro.";
    }

    /**
     * Obtiene la lista completa de libros.
     *
     * @return lista de todos los libros
     */
    public List<Libro> obtenerTodos() {
        return libroDAO.obtenerTodos();
    }

    /**
     * Busca libros por coincidencia parcial de título.
     *
     * @param titulo texto a buscar
     * @return lista de libros que coinciden, o todos si el texto está vacío
     */
    public List<Libro> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return libroDAO.obtenerTodos();
        }
        return libroDAO.buscarPorTitulo(titulo.trim());
    }

    /**
     * Actualiza un libro existente tras validar los datos.
     *
     * @param id                    identificador del libro a actualizar
     * @param titulo                nuevo título
     * @param autor                 nuevo autor
     * @param anioPublicacion       nuevo año de publicación
     * @param ejemplaresDisponibles nuevo número de ejemplares
     * @return mensaje de resultado
     */
    public String actualizarLibro(int id, String titulo, String autor, int anioPublicacion, int ejemplaresDisponibles) {
        if (id <= 0) {
            return "ID de libro no válido.";
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            return "El título no puede estar vacío.";
        }
        if (autor == null || autor.trim().isEmpty()) {
            return "El autor no puede estar vacío.";
        }
        if (anioPublicacion < 0 || anioPublicacion > 2100) {
            return "El año de publicación no es válido.";
        }
        if (ejemplaresDisponibles < 0) {
            return "El número de ejemplares no puede ser negativo.";
        }

        Libro libro = new Libro(id, titulo.trim(), autor.trim(), anioPublicacion, ejemplaresDisponibles);
        boolean exito = libroDAO.actualizar(libro);

        return exito ? "✔ Libro actualizado exitosamente." : "✘ Error al actualizar el libro.";
    }

    /**
     * Elimina un libro por su identificador.
     *
     * @param id identificador del libro
     * @return mensaje de resultado
     */
    public String eliminarLibro(int id) {
        if (id <= 0) {
            return "ID de libro no válido.";
        }
        boolean exito = libroDAO.eliminar(id);
        return exito ? "✔ Libro eliminado exitosamente." : "✘ Error al eliminar el libro.";
    }
}
