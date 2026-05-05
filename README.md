# 📚 Sistema de Gestión de Biblioteca

Aplicación de escritorio desarrollada en **JavaFX** para administrar el catálogo de libros de una biblioteca universitaria. Este proyecto implementa operaciones **CRUD** (Crear, Leer, Actualizar, Eliminar) utilizando una base de datos **SQLite** y siguiendo una **arquitectura multicapa** limpia.

## ✨ Características

- 📥 **Registrar libros**: Permite ingresar título, autor, año de publicación y número de ejemplares disponibles.
- 📋 **Consultar catálogo**: Visualización interactiva de todos los libros en una tabla dinámica.
- 🔍 **Búsqueda**: Buscador en tiempo real para encontrar libros por su título parcial o exacto.
- ✏️ **Actualizar**: Carga automática de los datos de un libro en el formulario para su edición.
- 🗑️ **Eliminar**: Borrado de registros con validación/confirmación previa.
- 🎨 **Interfaz Moderna**: Diseño atractivo con modo oscuro, gradientes y retroalimentación visual (CSS personalizado).

## 🏗 Arquitectura Multicapa

El proyecto está organizado en 6 capas claramente definidas para facilitar el mantenimiento y escalabilidad del código:

1. **Vista (FXML + CSS)**: Interfaz de usuario de la aplicación.
2. **Controller (`LibroController`)**: Maneja los eventos de la UI y conecta con la lógica de negocio.
3. **Service (`LibroService`)**: Contiene las reglas de negocio y validaciones de los datos antes de operar en la base de datos.
4. **DAO (`LibroDAO`)**: Data Access Object, maneja las consultas SQL crudas con `PreparedStatement` por seguridad.
5. **Database (`DatabaseConnection`)**: Configura el patrón Singleton para la conexión JDBC hacia SQLite y auto-crea las tablas.
6. **Modelo (`Libro`)**: Entidad (POJO) representativa del libro.

## 🛠 Tecnologías Utilizadas

- **Java 11+**
- **JavaFX 13** (Interfaz Gráfica)
- **SQLite** (Base de datos embebida)
- **JDBC** (`org.xerial:sqlite-jdbc`)
- **Maven** (Gestor de dependencias y construcción)

## 🚀 Cómo ejecutar el proyecto

Asegúrate de tener instalado Java Development Kit (JDK) y Maven en tu sistema.

1. Clona el repositorio:
   ```bash
   git clone https://github.com/BrandonCapacho/biblioteca-java.git
   ```
2. Navega al directorio del proyecto:
   ```bash
   cd biblioteca-java
   ```
3. Compila y ejecuta con Maven:
   ```bash
   mvn compile javafx:run
   ```

*(Nota: Al ejecutar por primera vez, el sistema creará automáticamente el archivo de base de datos local `biblioteca.db` en la raíz).*
