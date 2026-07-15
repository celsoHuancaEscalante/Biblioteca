
package LogicaChelso;

import ClaseBase.*;
import DatosChelso.*;
import java.util.*;

public class LogicaBuscar {
    
    private LibroAD libroAD = new LibroAD();
    private GeneroAD generoAD = new GeneroAD();
    private AutorAD autorAD = new AutorAD();
    private EditorialAD editorialAD = new EditorialAD();
    private EjemplarAD ejemplarAD = new EjemplarAD();
    
    // Retorna los datos para el ComboBox de Géneros
    public List<String> obtenerGeneros() {
        List<String> generos = new ArrayList<>();
        generos.add("Todos");
        for (Genero g : generoAD.obtenerTodos()) {
            generos.add(g.getNombre());
        }
        return generos;
    }
    
    // Retorna los datos para el ComboBox de Autores
    public List<String> obtenerAutores() {
        List<String> autores = new ArrayList<>();
        autores.add("Todos");
    
    // ========== OBTENER SOLO AUTORES CON LIBROS ==========
        List<Libro> todosLosLibros = libroAD.obtenerTodos();
        List<Integer> idsAutoresConLibros = new ArrayList<>();
    
    // Recopilar IDs de autores que tienen libros
        for (Libro libro : todosLosLibros) {
            int idAutor = libro.getAutor().getIdAutor();
            if (!idsAutoresConLibros.contains(idAutor)) {
                idsAutoresConLibros.add(idAutor);
            }
        }
    
    // Agregar solo esos autores al combo
        for (Autor a : autorAD.obtenerTodos()) {
            if (idsAutoresConLibros.contains(a.getIdAutor())) {
                autores.add(a.toString());
            }
        }
    
        return autores;
    }
    
    // Retorna todos los libros con sus datos formateados
    public List<Object[]> obtenerLibrosParaTabla() {
        List<Object[]> filas = new ArrayList<>();
        List<Libro> libros = libroAD.obtenerTodos();
        
        for (Libro libro : libros) {
            
            Object[] fila = {
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutor().toString(),
                libro.getEditorial().getNombre(),
                libro.getGenero().getNombre(),
                libro.getStock(),
                libro.getCategoria().getNombre()
            };
            filas.add(fila);
        }
        return filas;
    }
    
    // Buscar libros por título en tiempo real
    public List<Object[]> buscarPorTitulo(String titulo) {
        if (titulo.isEmpty()) {
            return obtenerLibrosParaTabla();
        }
        
        List<Object[]> filas = new ArrayList<>();
        List<Libro> libros = libroAD.buscarPorTitulo(titulo);
        
        for (Libro libro : libros) {
            
            Object[] fila = {
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutor().toString(),
                libro.getEditorial().getNombre(),
                libro.getGenero().getNombre(),
                libro.getStock(),
                libro.getCategoria().getNombre() 
            };
            filas.add(fila);
        }
        return filas;
    }
    
    // Filtrar por género y autor
    public List<Object[]> filtrarLibros(String generoSeleccionado, String autorSeleccionado) {
        Genero genero = null;
        Autor autor = null;
        
        if (!generoSeleccionado.equals("Todos")) {
            for (Genero g : generoAD.obtenerTodos()) {
                if (g.getNombre().equals(generoSeleccionado)) {
                    genero = g;
                    break;
                }
            }
        }
        
        if (!autorSeleccionado.equals("Todos")) {
            for (Autor a : autorAD.obtenerTodos()) {
                if (a.toString().equals(autorSeleccionado)) {
                    autor = a;
                    break;
                }
            }
        }
        
        List<Object[]> filas = new ArrayList<>();
        List<Libro> libros = libroAD.filtrar(genero, autor);
        
        for (Libro libro : libros) {
            
            Object[] fila = {
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutor().toString(),
                libro.getEditorial().getNombre(),
                libro.getGenero().getNombre(),
                libro.getStock(),
                libro.getCategoria().getNombre()
            };
            filas.add(fila);
        }
        return filas;
    }
    
    public List<Object[]> filtrarLibrosAvanzado(String tituloBuscado, String autorBuscado, String generoSeleccionado) {
    Genero genero = null;
    
    // Buscar el género si no es "Todos"
    if (!generoSeleccionado.equals("Todos") && !generoSeleccionado.isEmpty()) {
        for (Genero g : generoAD.obtenerTodos()) {
            if (g.getNombre().equals(generoSeleccionado)) {
                genero = g;
                break;
            }
        }
    }
    
    // Obtener todos los libros y filtrar
    List<Object[]> filas = new ArrayList<>();
    List<Libro> libros = libroAD.obtenerTodos();
    
    for (Libro libro : libros) {
        // Verificar coincidencia de título
        boolean coincideTitulo = true;
        if (!tituloBuscado.isEmpty()) {
            coincideTitulo = libro.getTitulo().toLowerCase().startsWith(tituloBuscado.toLowerCase());
        }
        
        // Verificar coincidencia de autor (BUSCAR DIRECTAMENTE EN EL LIBRO)
        boolean coincideAutor = true;
        if (!autorBuscado.isEmpty()) {
            coincideAutor = libro.getAutor().toString().toLowerCase().startsWith(autorBuscado.toLowerCase());
        }
        
        // Verificar coincidencia de género
        boolean coincideGenero = true;
        if (genero != null) {
            coincideGenero = libro.getGenero().getIdGenero() == genero.getIdGenero();
        }
        
        // Si coinciden los 3, agregar a resultados
        if (coincideTitulo && coincideAutor && coincideGenero) {
            Object[] fila = {
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutor().toString(),
                libro.getEditorial().getNombre(),
                libro.getGenero().getNombre(),
                libro.getStock(),
                libro.getCategoria().getNombre()
            };
            filas.add(fila);
        }
    }
    
    return filas;
}
    
    public int calcularTotalEjemplares(List<Object[]> filas) {
        int total = 0;
        for (Object[] fila : filas) {
            total += (Integer) fila[5]; // Columna "Stock"
        }
        return total;
    }
    
}

