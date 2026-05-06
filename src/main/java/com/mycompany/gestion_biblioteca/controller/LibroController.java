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

    // ─── Labels ─────────────────────────────────────────────────────
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

        // Enlazar lista a tabla
        tablaLibros.setItems(listaLibros);

        // Selección de fila -> cargar en formulario
        tablaLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) cargarSeleccionado(newSel);
        });

        // Validación preventiva: solo números
        soloNumeros(txtAnio, 4);        // año hasta 4 dígitos
        soloNumeros(txtEjemplares, 9);  // ejemplares hasta 9 dígitos

        // Cargar todo al iniciar
        mostrarTodos();
    }

    /** Evita que se escriban caracteres no numéricos en un TextField */
    private void soloNumeros(TextField tf, int maxLen) {
        tf.setTextFormatter(new TextFormatter<String>(change -> {
            String nuevo = change.getControlNewText();
            return nuevo.matches("\\d{0," + maxLen + "}") ? change : null;
        }));
    }

    /** Carga los datos del libro seleccionado en el formulario para edición. */
    private void cargarSeleccionado(Libro libro) {
        libroSeleccionadoId = libro.getId();
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
        txtEjemplares.setText(String.valueOf(libro.getEjemplaresDisponibles()));
        lblFormTitulo.setText("Actualizar Libro");
        mostrarEstado("Libro #" + libro.getId() + " cargado para edición.", false);
    }

    // ─── Validaciones simples ────────────────────────────────────────
    private boolean validarFormulario() {
        String titulo = safe(txtTitulo.getText());
        String autor  = safe(txtAutor.getText());
        String anioS  = safe(txtAnio.getText());
        String ejeS   = safe(txtEjemplares.getText());

        if (titulo.isEmpty()) {
            mostrarEstado("✘ El título es obligatorio.", true);
            txtTitulo.requestFocus();
            return false;
        }
        if (titulo.length() < 2) {
            mostrarEstado("✘ El título debe tener al menos 2 caracteres.", true);
            txtTitulo.requestFocus();
            return false;
        }

        if (autor.isEmpty()) {
            mostrarEstado("✘ El autor es obligatorio.", true);
            txtAutor.requestFocus();
            return false;
        }
        if (autor.length() < 2) {
            mostrarEstado("✘ El autor debe tener al menos 2 caracteres.", true);
            txtAutor.requestFocus();
            return false;
        }

        if (anioS.isEmpty()) {
            mostrarEstado("✘ El año es obligatorio.", true);
            txtAnio.requestFocus();
            return false;
        }
        int anio;
        try {
            anio = Integer.parseInt(anioS);
        } catch (NumberFormatException e) {
            mostrarEstado("✘ El año debe ser un número entero.", true);
            txtAnio.requestFocus();
            return false;
        }
        if (anio < 1400 || anio > 2100) {
            mostrarEstado("✘ El año debe estar entre 1400 y 2100.", true);
            txtAnio.requestFocus();
            return false;
        }

        if (ejeS.isEmpty()) {
            mostrarEstado("✘ Los ejemplares son obligatorios.", true);
            txtEjemplares.requestFocus();
            return false;
        }
        int ejemplares;
        try {
            ejemplares = Integer.parseInt(ejeS);
        } catch (NumberFormatException e) {
            mostrarEstado("✘ Los ejemplares deben ser un número entero.", true);
            txtEjemplares.requestFocus();
            return false;
        }
        if (ejemplares <= 0) {
            mostrarEstado("✘ Los ejemplares deben ser mayores que 0.", true);
            txtEjemplares.requestFocus();
            return false;
        }

        return true;
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }

    // ─── Acciones ───────────────────────────────────────────────────

    /** Registra un nuevo libro con los datos del formulario. */
    @FXML
    private void registrarLibro() {

        if (!validarFormulario()) return;

        String titulo = safe(txtTitulo.getText());
        String autor  = safe(txtAutor.getText());
        int anio      = Integer.parseInt(safe(txtAnio.getText()));
        int ejemplares= Integer.parseInt(safe(txtEjemplares.getText()));

        String resultado = libroService.registrarLibro(titulo, autor, anio, ejemplares);

        boolean esError = resultado != null && resultado.startsWith("✘");
        mostrarEstado(resultado, esError);

        if (!esError) {
            limpiarCampos();
            mostrarTodos();
        }
    }

    /** Carga y muestra todos los libros en la tabla. */
    @FXML
    private void mostrarTodos() {
        List<Libro> libros = libroService.obtenerTodos();
        listaLibros.setAll(libros);
        mostrarEstado("Se encontraron " + libros.size() + " libro(s).", false);
        if (txtBusqueda != null) txtBusqueda.clear();
    }

    /** Busca libros por título y muestra los resultados en la tabla. */
    @FXML
    private void buscarPorTitulo() {
        String titulo = safe(txtBusqueda.getText());

        if (titulo.isEmpty()) {
            mostrarTodos();
            return;
        }

        List<Libro> resultados = libroService.buscarPorTitulo(titulo);
        listaLibros.setAll(resultados);
        mostrarEstado("Búsqueda: " + resultados.size() + " resultado(s) para \"" + titulo + "\".", false);
    }

    /** Elimina el libro seleccionado en la tabla. */
    @FXML
    private void eliminarLibro() {
        Libro seleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarEstado("✘ Seleccione un libro de la tabla para eliminar.", true);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Eliminar el libro?");
        alert.setContentText("\"" + seleccionado.getTitulo() + "\" será eliminado permanentemente.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String resultado = libroService.eliminarLibro(seleccionado.getId());
                boolean esError = resultado != null && resultado.startsWith("✘");
                mostrarEstado(resultado, esError);
                if (!esError) mostrarTodos();
            }
        });
    }

    /** Actualiza el libro actualmente cargado en el formulario. */
    @FXML
    private void actualizarLibro() {
        if (libroSeleccionadoId <= 0) {
            mostrarEstado("✘ Seleccione un libro de la tabla para actualizar.", true);
            return;
        }

        if (!validarFormulario()) return;

        String titulo = safe(txtTitulo.getText());
        String autor  = safe(txtAutor.getText());
        int anio      = Integer.parseInt(safe(txtAnio.getText()));
        int ejemplares= Integer.parseInt(safe(txtEjemplares.getText()));

        String resultado = libroService.actualizarLibro(libroSeleccionadoId, titulo, autor, anio, ejemplares);
        boolean esError = resultado != null && resultado.startsWith("✘");
        mostrarEstado(resultado, esError);

        if (!esError) {
            limpiarCampos();
            mostrarTodos();
        }
    }

    /** Limpia todos los campos del formulario de registro. */
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

    /** Muestra un mensaje de estado en la barra inferior. */
    private void mostrarEstado(String mensaje, boolean esError) {
        lblEstado.setText(mensaje);
        if (esError) {
            lblEstado.setStyle("-fx-text-fill: #ff6b6b;");
        } else {
            lblEstado.setStyle("-fx-text-fill: #51cf66;");
        }
    }
}