package com.mycompany.gestion_biblioteca.model;

/**
 * Modelo que representa un libro en el catálogo de la biblioteca.
 */
public class Libro {

    private int id;
    private String titulo;
    private String autor;
    private int anioPublicacion;
    private int ejemplaresDisponibles;

    /** Constructor vacío. */
    public Libro() {
    }

    /**
     * Constructor completo.
     *
     * @param id                    identificador único
     * @param titulo                título del libro
     * @param autor                 autor del libro
     * @param anioPublicacion       año de publicación
     * @param ejemplaresDisponibles número de ejemplares disponibles
     */
    public Libro(int id, String titulo, String autor, int anioPublicacion, int ejemplaresDisponibles) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    /**
     * Constructor sin id (para nuevos registros).
     *
     * @param titulo                título del libro
     * @param autor                 autor del libro
     * @param anioPublicacion       año de publicación
     * @param ejemplaresDisponibles número de ejemplares disponibles
     */
    public Libro(String titulo, String autor, int anioPublicacion, int ejemplaresDisponibles) {
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    // ─── Getters y Setters ──────────────────────────────────────────

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public int getEjemplaresDisponibles() {
        return ejemplaresDisponibles;
    }

    public void setEjemplaresDisponibles(int ejemplaresDisponibles) {
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anioPublicacion=" + anioPublicacion +
                ", ejemplaresDisponibles=" + ejemplaresDisponibles +
                '}';
    }
}
