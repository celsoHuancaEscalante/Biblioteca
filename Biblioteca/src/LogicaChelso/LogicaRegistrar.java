
package LogicaChelso;

import DatosChelso.*;
import ClaseBase.*;
import java.util.*;

public class LogicaRegistrar {
    
    
    private LibroAD libroAD = new LibroAD();
    private AutorAD autorAD = new AutorAD();
    private EditorialAD editorialAD = new EditorialAD();
    private GeneroAD generoAD = new GeneroAD();
    private EjemplarAD ejemplarAD = new EjemplarAD();
    private CategoriaAD categoriaAD = new CategoriaAD();
    
    
    public List<Object[]> obtenerLibrosParaTabla() {
        List<Object[]> filas = new ArrayList<>();
        List<Libro> libros = libroAD.obtenerTodos();
        
        for (Libro libro : libros) {
            Object[] fila = {
                libro.getTitulo(),
                libro.getAutor().toString(),
                libro.getEditorial().getNombre(),
                libro.getAnioPublicacion(),
                libro.getGenero().getNombre(),
                libro.getStock()
            };
            filas.add(fila);
        }
        return filas;
    }
    
    
    public Libro buscarLibroPorTitulo(String titulo) {
        List<Libro> libros = libroAD.obtenerTodos();
        for (Libro libro : libros) {
            if (libro.getTitulo().equals(titulo)) {
                return libro;
            }
        }
        return null;
    }
    
    private Categoria calcularCategoria(int anoPublicacion) {
        int añoActual = java.time.LocalDate.now().getYear();
        int antiguedad = añoActual - anoPublicacion;
        
        String nombreCategoria;
        if (antiguedad <= 15) {
            nombreCategoria = "Nuevo";
        } else if (antiguedad <= 35) {
            nombreCategoria = "Regular";
        } else {
            nombreCategoria = "Antiguo";
        }
        
        // Obtener de BD
        Categoria categoria = categoriaAD.obtenerPorNombre(nombreCategoria);
        if (categoria == null) {
            // Fallback (aunque debería existir en BD)
            categoria = new Categoria(0, nombreCategoria);
        }
        
        return categoria;
    }
    
    //GUARDAR LIBRO (INSERTAR O ACTUALIZAR)
    public String guardarLibro(String titulo, String nombreAutor, String nombreEditorial,
                               int ano, String nombreGenero, int stock, Libro libroActual) {
        
        try {
            // VERIFICAR O CREAR AUTOR
            Autor autor = verificarOCrearAutor(nombreAutor);
            if (autor == null) {
                return "Error al procesar el autor";
            }
            
            // VERIFICAR O CREAR EDITORIAL
            Editorial editorial = verificarOCrearEditorial(nombreEditorial);
            if (editorial == null) {
                return "Error al procesar la editorial";
            }
            
            // OBTENER GÉNERO DE LA BD
            Genero genero = obtenerGenero(nombreGenero);
                if (genero == null) {
                return "El género seleccionado no existe en la base de datos";
            }
            
            Categoria categoria = calcularCategoria(ano);    
                
            // INSERTAR O ACTUALIZAR LIBRO
            if (libroActual != null) {
                // MODIFICAR LIBRO EXISTENTE
                libroActual.setTitulo(titulo);
                libroActual.setAutor(autor);
                libroActual.setEditorial(editorial);
                libroActual.setAnioPublicacion(ano);
                libroActual.setGenero(genero);
                libroActual.setCategoria(categoria);
                libroActual.setStock(stock);
                
                boolean actualizado = libroAD.actualizar(libroActual);
                
                if (actualizado) {
                    return "Libro actualizado correctamente";
                } else {
                    return "Error al actualizar el libro";
                }
            } else {
                // INSERTAR NUEVO LIBRO
                Libro nuevoLibro = new Libro(0, genero, autor, editorial, titulo, stock, ano, categoria);
                int idLibroInsertado = libroAD.insertar(nuevoLibro);
                
                if (idLibroInsertado > 0) {
                    boolean ejemplaresCreados = ejemplarAD.insertarEjemplares(idLibroInsertado, stock);
                    
                    if (ejemplaresCreados) {
                        return "Libro registrado con " + stock + " ejemplares";
                    } else {
                        return "Error al crear los ejemplares";
                    }
                } else {
                    return "Error al insertar el libro";
                }
            }
            
        } catch (NumberFormatException e) {
            return "Verifica que Año y Stock sean números";
        }
    }
    
    
    private Autor verificarOCrearAutor(String nombreAutor) {
        List<Autor> autoresEnBD = autorAD.obtenerTodos();
        
        // Buscar si ya existe
        for (Autor a : autoresEnBD) {
            if (a.toString().equalsIgnoreCase(nombreAutor)) {
                return a;
            }
        }
        
        // Si no existe, crear uno nuevo
        String[] partes = nombreAutor.trim().split(" ");
        
        String primerNombre = partes.length > 0 ? partes[0] : "";
        String segundoNombre = partes.length > 1 ? partes[1] : "";
        String primerApellido = partes.length > 2 ? partes[2] : "";
        String segundoApellido = partes.length > 3 ? partes[3] : "";
        
        Autor nuevoAutor = new Autor(0, primerNombre, segundoNombre, primerApellido, segundoApellido);
        autorAD.insertar(nuevoAutor);
        
        // Obtener el ID asignado
        autoresEnBD = autorAD.obtenerTodos();
        for (Autor a : autoresEnBD) {
            if (a.mostrarDatos().equals(nombreAutor)) {
                return a;
            }
        }
        return nuevoAutor;
    }
    
    private Editorial verificarOCrearEditorial(String nombreEditorial) {
        List<Editorial> editorialesEnBD = editorialAD.obtenerTodos();
        
        // Buscar si ya existe
        for (Editorial e : editorialesEnBD) {
            if (e.getNombre().equalsIgnoreCase(nombreEditorial)) {
                return e;
            }
        }
        
        // Si no existe, crear una nueva
        Editorial nuevaEditorial = new Editorial(0, nombreEditorial);
        editorialAD.insertar(nuevaEditorial);
        
        // Obtener el ID asignado
        editorialesEnBD = editorialAD.obtenerTodos();
        for (Editorial e : editorialesEnBD) {
            if (e.getNombre().equals(nombreEditorial)) {
                return e;
            }
        }
        return nuevaEditorial;
    }
    
    public List<String> obtenerGenerosParaComboBox() {
        List<String> generos = new ArrayList<>();
        for (Genero g : generoAD.obtenerTodos()) {
            generos.add(g.getNombre());
        }
        return generos;
    }

// Método simplificado para obtener un género por nombre
    private Genero obtenerGenero(String nombreGenero) {
        List<Genero> generosEnBD = generoAD.obtenerTodos();
    
        for (Genero g : generosEnBD) {
            if (g.getNombre().equalsIgnoreCase(nombreGenero)) {
                return g;
            }
        }
        return null;  // No encontrado
    }
    
    public String eliminarLibro(String titulo) {
        List<Libro> libros = libroAD.obtenerTodos();
        
        for (Libro libro : libros) {
            if (libro.getTitulo().equals(titulo)) {
                // Eliminar ejemplares primero
                ejemplarAD.eliminarPorLibro(libro.getIdLibro());
                
                // Eliminar libro
                boolean eliminado = libroAD.eliminar(libro.getIdLibro());
                
                if (eliminado) {
                    return "Libro eliminado correctamente";
                } else {
                    return "Error al eliminar el libro";
                }
            }
        }
        return "Libro no encontrado";
    }
}
