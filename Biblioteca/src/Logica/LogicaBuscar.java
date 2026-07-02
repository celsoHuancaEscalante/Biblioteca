
package Logica;

import ClaseBase.*;
import Datos.*;
import java.util.List;

public class LogicaBuscar {
    
    private LibroAD libroAD = new LibroAD();
    private GeneroAD generoAD = new GeneroAD();
    private AutorAD autorAD = new AutorAD();
    private EditorialAD editorialAD = new EditorialAD();
    private EjemplarAD ejemplarAD = new EjemplarAD();
    
    // Retorna los datos para el ComboBox de Géneros
    public java.util.List<String> obtenerGeneros() {
        java.util.List<String> generos = new java.util.ArrayList<>();
        generos.add("Todos");
        for (Genero g : generoAD.obtenerTodos()) {
            generos.add(g.getNombre());
        }
        return generos;
    }
    
    // Retorna los datos para el ComboBox de Autores
    public java.util.List<String> obtenerAutores() {
        java.util.List<String> autores = new java.util.ArrayList<>();
        autores.add("Todos");
        for (Autor a : autorAD.obtenerTodos()) {
            autores.add(a.toString());
        }
        return autores;
    }
    
    // Retorna todos los libros con sus datos formateados
    public java.util.List<Object[]> obtenerLibrosParaTabla() {
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        List<Libro> libros = libroAD.obtenerTodos();
        
        for (Libro libro : libros) {
            int disponibles = ejemplarAD.contarDisponibles(libro.getIdLibro());
            String estado = disponibles > 0 ? "Disponible" : "No Disponible";
            
            Object[] fila = {
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutor().toString(),
                libro.getEditorial().getNombre(),
                libro.getGenero().getNombre(),
                libro.getStock(),
                estado
            };
            filas.add(fila);
        }
        return filas;
    }
    
    // Buscar libros por título en tiempo real
    public java.util.List<Object[]> buscarPorTitulo(String titulo) {
        if (titulo.isEmpty()) {
            return obtenerLibrosParaTabla();
        }
        
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        List<Libro> libros = libroAD.buscarPorTitulo(titulo);
        
        for (Libro libro : libros) {
            int disponibles = ejemplarAD.contarDisponibles(libro.getIdLibro());
            String estado = disponibles > 0 ? "Disponible" : "No Disponible";
            
            Object[] fila = {
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutor().toString(),
                libro.getEditorial().getNombre(),
                libro.getGenero().getNombre(),
                libro.getStock(),
                estado
            };
            filas.add(fila);
        }
        return filas;
    }
    
    // Filtrar por género y autor
    public java.util.List<Object[]> filtrarLibros(String generoSeleccionado, String autorSeleccionado) {
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
        
        java.util.List<Object[]> filas = new java.util.ArrayList<>();
        List<Libro> libros = libroAD.filtrar(genero, autor);
        
        for (Libro libro : libros) {
            int disponibles = ejemplarAD.contarDisponibles(libro.getIdLibro());
            String estado = disponibles > 0 ? "Disponible" : "No Disponible";
            
            Object[] fila = {
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutor().toString(),
                libro.getEditorial().getNombre(),
                libro.getGenero().getNombre(),
                libro.getStock(),
                estado
            };
            filas.add(fila);
        }
        return filas;
    }
    
    // Calcular total de ejemplares
    public int calcularTotalEjemplares(java.util.List<Object[]> filas) {
        int total = 0;
        for (Object[] fila : filas) {
            total += (Integer) fila[5]; // Columna "Stock"
        }
        return total;
    }
    
}
