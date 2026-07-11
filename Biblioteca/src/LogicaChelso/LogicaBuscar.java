
package LogicaChelso;

import DatosChelso.EjemplarAD;
import DatosChelso.EditorialAD;
import DatosChelso.AutorAD;
import DatosChelso.GeneroAD;
import DatosChelso.LibroAD;
import ClaseBase.*;
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
        for (Autor a : autorAD.obtenerTodos()) {
            autores.add(a.toString());
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
    
    
    public int calcularTotalEjemplares(List<Object[]> filas) {
        int total = 0;
        for (Object[] fila : filas) {
            total += (Integer) fila[5]; // Columna "Stock"
        }
        return total;
    }
    
}
