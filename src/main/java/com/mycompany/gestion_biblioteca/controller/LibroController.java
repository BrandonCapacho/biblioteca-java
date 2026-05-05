package com.mycompany.gestion_biblioteca.controller;

import com.mycompany.gestion_biblioteca.model.Libro;
import com.mycompany.gestion_biblioteca.service.LibroService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de la vista principal del sistema de gestión de biblioteca.
 * Maneja las interacciones del usuario con el formulario y la tabla de libros.
 */
public class LibroController implements Initializable {

    // ─── Formulario de registro ─────────────────────────────────────
    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtAnio;
    @FXML private TextField txtEjemplares;

    // ─── Búsqueda ───────────────────────────────────────────────────
    @FXML private TextField txtBusqueda;

    // ─── Tabla ──────────────────────────────────────────────────────
    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro, Integer> colId;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colAutor;
    @FXML private TableColumn<Libro, Integer> colAnio;
    @FXML private TableColumn<Libro, Integer> colEjemplares;

    // ─── Labels ──────────────────────────────────────────────────────
    @FXML private Label lblEstado;
    @FXML private Label lblFormTitulo;

    // ─── Servicio ───────────────────────────────────────────────────
    private final LibroService libroService = new LibroService();
    private final ObservableList<Libro> listaLibros = FXCollections.observableArrayList();
    private int libroSeleccionadoId = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));
        colEjemplares.setCellValueFactory(new PropertyValueFactory<>("ejemplaresDisponibles"));

        tablaLibros.setItems(listaLibros);

        // Listener: al seleccionar una fila, cargar datos en el formulario
        tablaLibros.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> cargarSeleccionado(newVal));

        // Cargar datos iniciales
        mostrarTodos();
    }

    // ─── Selección de tabla ──────────────────────────────────────────

    /**
     * Carga los datos del libro seleccionado en el formulario para edición.
     */
    private void cargarSeleccionado(Libro libro) {
        if (libro != null) {
            libroSeleccionadoId = libro.getId();
            txtTitulo.setText(libro.getTitulo());
            txtAutor.setText(libro.getAutor());
            txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
            txtEjemplares.setText(String.valueOf(libro.getEjemplaresDisponibles()));
            lblFormTitulo.setText("Actualizar Libro");
            mostrarEstado("Libro #" + libro.getId() + " cargado para edición.", false);
        }
    }

    // ─── Acciones ───────────────────────────────────────────────────

    /**
     * Registra un nuevo libro con los datos del formulario.
     */
    @FXML
    private void registrarLibro() {
        String titulo = txtTitulo.getText();
        String autor = txtAutor.getText();
        String anioStr = txtAnio.getText();
        String ejemplaresStr = txtEjemplares.getText();

        // Validar campos numéricos
        int anio;
        int ejemplares;
        try {
            anio = Integer.parseInt(anioStr);
        } catch (NumberFormatException e) {
            mostrarEstado("✘ El año debe ser un número entero.", true);
            return;
        }
        try {
            ejemplares = Integer.parseInt(ejemplaresStr);
        } catch (NumberFormatException e) {
            mostrarEstado("✘ Los ejemplares deben ser un número entero.", true);
            return;
        }

        String resultado = libroService.registrarLibro(titulo, autor, anio, ejemplares);
        boolean esError = resultado.startsWith("✘") || !resultado.startsWith("✔");
        mostrarEstado(resultado, esError);

        if (!esError) {
            libroSeleccionadoId = -1;
            limpiarCampos();
            mostrarTodos();
        }
    }

    /**
     * Carga y muestra todos los libros en la tabla.
     */
    @FXML
    private void mostrarTodos() {
        List<Libro> libros = libroService.obtenerTodos();
        listaLibros.setAll(libros);
        mostrarEstado("Se encontraron " + libros.size() + " libro(s).", false);
        txtBusqueda.clear();
    }

    /**
     * Busca libros por título y muestra los resultados en la tabla.
     */
    @FXML
    private void buscarPorTitulo() {
        String titulo = txtBusqueda.getText();
        List<Libro> resultados = libroService.buscarPorTitulo(titulo);
        listaLibros.setAll(resultados);

        if (titulo != null && !titulo.trim().isEmpty()) {
            mostrarEstado("Búsqueda: " + resultados.size() + " resultado(s) para \"" + titulo.trim() + "\".", false);
        }
    }

    /**
     * Elimina el libro seleccionado en la tabla.
     */
    @FXML
    private void eliminarLibro() {
        Libro seleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarEstado("✘ Seleccione un libro de la tabla para eliminar.", true);
            return;
        }

        // Confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Eliminar el libro?");
        alert.setContentText("\"" + seleccionado.getTitulo() + "\" será eliminado permanentemente.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String resultado = libroService.eliminarLibro(seleccionado.getId());
                boolean esError = resultado.startsWith("✘");
                mostrarEstado(resultado, esError);
                if (!esError) {
                    mostrarTodos();
                }
            }
        });
    }

    /**
     * Actualiza el libro actualmente cargado en el formulario.
     */
    @FXML
    private void actualizarLibro() {
        if (libroSeleccionadoId <= 0) {
            mostrarEstado("✘ Seleccione un libro de la tabla para actualizar.", true);
            return;
        }

        String titulo = txtTitulo.getText();
        String autor = txtAutor.getText();
        int anio;
        int ejemplares;
        try {
            anio = Integer.parseInt(txtAnio.getText());
        } catch (NumberFormatException e) {
            mostrarEstado("✘ El año debe ser un número entero.", true);
            return;
        }
        try {
            ejemplares = Integer.parseInt(txtEjemplares.getText());
        } catch (NumberFormatException e) {
            mostrarEstado("✘ Los ejemplares deben ser un número entero.", true);
            return;
        }

        String resultado = libroService.actualizarLibro(libroSeleccionadoId, titulo, autor, anio, ejemplares);
        boolean esError = resultado.startsWith("✘");
        mostrarEstado(resultado, esError);

        if (!esError) {
            libroSeleccionadoId = -1;
            limpiarCampos();
            mostrarTodos();
        }
    }

    /**
     * Limpia todos los campos del formulario de registro.
     */
    @FXML
    private void limpiarCampos() {
        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtEjemplares.clear();
        libroSeleccionadoId = -1;
        lblFormTitulo.setText("Registrar Libro");
        tablaLibros.getSelectionModel().clearSelection();
        txtTitulo.requestFocus();
    }

    // ─── Utilidades ─────────────────────────────────────────────────

    /**
     * Muestra un mensaje de estado en la barra inferior.
     *
     * @param mensaje texto a mostrar
     * @param esError {@code true} para estilo de error, {@code false} para éxito
     */
    private void mostrarEstado(String mensaje, boolean esError) {
        lblEstado.setText(mensaje);
        if (esError) {
            lblEstado.setStyle("-fx-text-fill: #ff6b6b;");
        } else {
            lblEstado.setStyle("-fx-text-fill: #51cf66;");
        }
    }
}
